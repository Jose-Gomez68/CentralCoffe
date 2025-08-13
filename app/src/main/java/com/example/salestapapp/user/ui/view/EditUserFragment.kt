package com.example.salestapapp.user.ui.view

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.R
import com.example.salestapapp.databinding.FragmentEditUserBinding
import com.example.salestapapp.login.data.UserRepository
import com.example.salestapapp.login.data.model.UsersModel
import com.example.salestapapp.rom.CyberCoffeAppDatabase
import com.example.salestapapp.rom.CyberCoffeDatabase
import com.example.salestapapp.user.data.domain.usecase.EditUserUseCase
import com.example.salestapapp.user.data.domain.usecase.GetUserByIDUseCase
import com.example.salestapapp.user.ui.viewmodel.EditUserViewModel
import com.example.salestapapp.user.ui.viewmodel.EditUserViewModelFactory
import com.example.salestapapp.util.UtilsFunctions
import java.io.ByteArrayOutputStream
import java.io.InputStream

class EditUserFragment : Fragment() {

    private var _binding: FragmentEditUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: CyberCoffeDatabase
    private lateinit var viewModel: EditUserViewModel
    private var listener: OnUserFragmentChangeListener? = null
    private lateinit var utilsFunctions: UtilsFunctions
    private var imageUser: String? = ""
    private var userID: Int = 0
    private var createdDate: String = ""

    /*val imagePickerMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.ivSelectImageEditUser.setImageURI(uri)
            imageUser = convertImageToByteArray(uri)
        }
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = CyberCoffeAppDatabase.CyberCoffeAppDatabase.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditUserBinding.inflate(inflater, container, false)
        initView()

        val editRepository: UserRepository = UserRepository(db)
        val viewModelProviderFactory = EditUserViewModelFactory(
            EditUserUseCase(editRepository),
            GetUserByIDUseCase(editRepository)
        )

        viewModel = ViewModelProvider(
            this,
            viewModelProviderFactory
        )[EditUserViewModel::class.java]

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userID = arguments?.getInt("userID") ?: return
        viewModel.getUser(userID)
        utilsFunctions = UtilsFunctions()

        viewModel.editUserModel.observe(viewLifecycleOwner) { result ->
            createdDate = result.createDate
            binding.etNameEditUser.setText(result.name)
            binding.etApEditUser.setText(result.lastName)
            binding.etUserNameEditUser.setText(result.userName)
            binding.etPasswordEditUser.setText(result.password)
            binding.etTelEditUser.setText(result.phone)
        }

        viewModel.editUserSucces.observe(viewLifecycleOwner) { result ->
            if (result.name.isNotEmpty()){
                requireActivity().onBackPressed()
            }
        }

    }

    private fun initView() {
        /*binding.ivSelectImageEditUser.setOnClickListener {
            imagePickerMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnDeleteImageEditUser.setOnClickListener {
            binding.ivSelectImageEditUser.setImageResource(R.drawable.gallery)
            imageUser = null
        }*/

        binding.btnReturnEditUser.setOnClickListener {
            utilsFunctions.showConfirmDialog(requireActivity(),
                getString(R.string.title_message_return_view_util),
                getString(R.string.message_return_view_util),
                getString(R.string.exit_message_return_view_util),
                getString(R.string.cancel_message_return_view_util),
                onConfirm = {
                    requireActivity().onBackPressed()
                },
                onCancel = {

                }
            )
        }

        binding.btnRegisterEditUser.setOnClickListener {
            if (validationForm()){
                editUser()
            }
        }

    }

    private fun validationForm(): Boolean {
        val etEmpty = "El campo no puede ser vacio"
        val regex = Regex("^\\d{10}$")
        if (binding.etNameEditUser.text.toString().isEmpty()){
            binding.etNameEditUser.error = etEmpty
            return false
        }else if (binding.etNameEditUser.text.toString().length <= 2){
            binding.etNameEditUser.error = "El nombre debe de ser mas de 2 caracteres"
            return false
        }else if (binding.etApEditUser.text.toString().isEmpty()){
            binding.etApEditUser.error = etEmpty
            return false
        }else if (binding.etApEditUser.text.toString().length <= 2){
            binding.etApEditUser.error = "El apellido debe de ser mas de 2 caracteres"
            return false
        }else if (binding.etUserNameEditUser.text.toString().isEmpty()){
            binding.etUserNameEditUser.error = etEmpty
            return false
        }else if (binding.etUserNameEditUser.text.toString().length <= 2){
            binding.etUserNameEditUser.error = "El nombre de usuario debe de ser mas de 2 caracteres"
            return false
        }else if (binding.etPasswordEditUser.text.toString().length < 6){
            binding.etPasswordEditUser.error = "La contraseña debe ser 6 caracteres"
            return false
        }else if (binding.etTelEditUser.text.toString().isEmpty()){
            binding.etTelEditUser.error = etEmpty
            return false
        }else if (!regex.matches(binding.etTelEditUser.text.toString())){
            binding.etTelEditUser.error = "El numero de telefono debe de ser 10 digitos"
            return false
        }

        binding.etNameEditUser.error = null
        binding.etApEditUser.error = null
        binding.etUserNameEditUser.error = null
        binding.etPasswordEditUser.error = null
        binding.etTelEditUser.error = null

        return true

    }

    private fun editUser() {
        val user = UsersModel(
            userID,
            binding.etNameEditUser.text.toString(),
            binding.etApEditUser.text.toString(),
            binding.etUserNameEditUser.text.toString(),
            binding.etPasswordEditUser.text.toString(),
            binding.etTelEditUser.text.toString(),
            createdDate,
            utilsFunctions.getCurrentFormattedDate()
        )

        viewModel.onUpdate(user)
    }

    private fun convertImageToByteArray(uri: Uri): String? {
        var inputStream: InputStream? = null
        var byteArrayOutputStream: ByteArrayOutputStream? = null
        try {
            val contentResolver: ContentResolver = requireContext().contentResolver
            inputStream = contentResolver.openInputStream(uri)
            if (inputStream != null) {
                val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
                return Base64.encodeToString(byteArray, Base64.DEFAULT)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
                byteArrayOutputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnUserFragmentChangeListener) {
            listener = context
            listener?.onUserFragmentChangeListener(this)
        } else {
            throw RuntimeException("$context must implement OnUserFragmentChangeListener")
        }
    }

}