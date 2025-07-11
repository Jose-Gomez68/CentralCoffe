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
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.databinding.FragmentNewUserBinding
import com.example.salestapapp.login.data.UserRepository
import com.example.salestapapp.login.data.model.UsersModel
import com.example.salestapapp.rom.CyberCoffeAppDatabase
import com.example.salestapapp.rom.CyberCoffeDatabase
import com.example.salestapapp.user.data.domain.usecase.SaveUserUseCase
import com.example.salestapapp.user.ui.viewmodel.NewUserViewModel
import com.example.salestapapp.user.ui.viewmodel.NewUserViewModelFactory
import com.example.salestapapp.util.UtilsFunctions
import java.io.ByteArrayOutputStream
import java.io.InputStream

class NewUserFragment : Fragment() {

    private lateinit var binding: FragmentNewUserBinding
    private var listener: OnUserFragmentChangeListener? = null
    private var user: UsersModel? = null
    private lateinit var  viewModel: NewUserViewModel
    private lateinit var db: CyberCoffeDatabase
    private lateinit var utilsFunctions: UtilsFunctions
//    private var imageUser: String? = ""

    /*SI VA USART IMAGEN SOLO DEBES DESCOMENTAR EL CODIGO DEL IMAGE VIEW
    DE XML Y ESTE CODIGO DE LA IMAGEVIEW EN MI CLASE KOTLIN

    val imagePickerMedia = registerForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.ivSelectImageNewUser.setImageURI(uri)
            imageUser = convertImageToByteArray(uri)
        }else{
            Toast.makeText(requireContext(), "No se selecciono una imagen", Toast.LENGTH_SHORT).show()
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
        binding = FragmentNewUserBinding.inflate(inflater, container, false)
        val repository = UserRepository(db)
        val viewModelFactory = NewUserViewModelFactory(SaveUserUseCase(repository))
        utilsFunctions = UtilsFunctions()
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[NewUserViewModel::class.java]

        binding.btnRegisterNewUser.setOnClickListener {
            saveUser()
        }

        binding.btnReturnNewUser.setOnClickListener {
            requireActivity().onBackPressed()
        }

        viewModel.newUserModel.observe(viewLifecycleOwner) { result ->
            if (result.name.isNotEmpty()){
                binding.etNameNewUser.setText("")
                binding.etApNewUser.setText("")
                binding.etUserNameNewUser.setText("")
                binding.etPhoneNewUser.setText("")
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    /*listener of the fragments to
    FloatingButton gone or visible*/
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnUserFragmentChangeListener) {
            listener = context
            listener?.onUserFragmentChangeListener(this)
        } else {
            throw RuntimeException("$context must implement OnUserFragmentChangeListener")
        }
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

    private fun validationForm(): Boolean {
        val etEmpty = "El campo no puede ser vacio"
        val regex = Regex("^\\d{10}$")
        if (binding.etNameNewUser.text.toString().isEmpty()){
            binding.etNameNewUser.error = etEmpty
            return false
        }else if (binding.etNameNewUser.text.toString().length <= 2){
            binding.etNameNewUser.error = "El nombre debe de ser mas de 2 caracteres"
            return false
        }else if (binding.etApNewUser.text.toString().isEmpty()){
            binding.etApNewUser.error = etEmpty
            return false
        }else if (binding.etApNewUser.text.toString().length <= 2){
            binding.etApNewUser.error = "El apellido debe de ser mas de 2 caracteres"
            return false
        }else if (binding.etUserNameNewUser.text.toString().isEmpty()){
            binding.etUserNameNewUser.error = etEmpty
            return false
        }else if (binding.etUserNameNewUser.text.toString().length <= 2){
            binding.etUserNameNewUser.error = "El nombre de usuario debe de ser mas de 2 caracteres"
            return false
        }else if (binding.etPhoneNewUser.text.toString().isEmpty()){
            binding.etPhoneNewUser.error = etEmpty
            return false
        }else if (!regex.matches(binding.etPhoneNewUser.text.toString())){
            binding.etPhoneNewUser.error = "El numero de telefono debe de ser 10 digitos"
            return false
        }

        binding.etNameNewUser.error = null
        binding.etApNewUser.error = null
        binding.etUserNameNewUser.error = null
        binding.etPhoneNewUser.error = null

        return true

    }

    private fun saveUser() {
        if (validationForm()){
            val user = UsersModel(
                0,
                binding.etNameNewUser.text.toString(),
                binding.etApNewUser.text.toString(),
                binding.etUserNameNewUser.text.toString(),
                binding.etPhoneNewUser.text.toString(),
                utilsFunctions.getCurrentFormattedDate(),
                utilsFunctions.getCurrentFormattedDate()
            )
            viewModel.onCreate(user)
        }
    }

}