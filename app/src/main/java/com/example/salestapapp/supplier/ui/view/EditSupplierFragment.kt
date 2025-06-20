package com.example.salestapapp.supplier.ui.view

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
import com.example.salestapapp.databinding.FragmentEditSupplierBinding
import com.example.salestapapp.rom.CyberCoffeAppDatabase
import com.example.salestapapp.rom.CyberCoffeDatabase
import com.example.salestapapp.supplier.data.domain.repository.SupplierRepository
import com.example.salestapapp.supplier.data.domain.usecase.EditSupplierUseCase
import com.example.salestapapp.supplier.data.domain.usecase.GetSupplierByIdUseCase
import com.example.salestapapp.supplier.data.model.SuppliersModel
import com.example.salestapapp.supplier.ui.viewmodel.EditSupplierViewModel
import com.example.salestapapp.supplier.ui.viewmodel.EditSupplierViewModelFactory
import com.example.salestapapp.util.UtilsFunctions
import java.io.ByteArrayOutputStream
import java.io.InputStream


class EditSupplierFragment : Fragment() {

    private var _binding: FragmentEditSupplierBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: CyberCoffeDatabase
    private lateinit var viewModel: EditSupplierViewModel
    private var listener: OnSupplierFragmentChangeListener? = null
    private lateinit var utilsFunctions: UtilsFunctions
    private var imageSupplier: String? = ""
    private var supplierID: Int = 0
    private var createdDate: String = ""

    val imagePickerMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.ivSelectImageEditSup.setImageURI(uri)
            imageSupplier = convertImageToByteArray(uri)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = CyberCoffeAppDatabase.CyberCoffeAppDatabase.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditSupplierBinding.inflate(inflater, container, false)
        initView()

        val editRepository: SupplierRepository = SupplierRepository(db)
        val viewModelProviderFactory = EditSupplierViewModelFactory(
            EditSupplierUseCase(editRepository),
            GetSupplierByIdUseCase(editRepository)
        )

        viewModel = ViewModelProvider(
            this,
            viewModelProviderFactory
        )[EditSupplierViewModel::class.java]

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        supplierID = arguments?.getInt("supplierID") ?: return
        viewModel.getSupplier(supplierID)
        utilsFunctions = UtilsFunctions()

        viewModel.supplierModel.observe(viewLifecycleOwner) { result ->
            createdDate = result.createDate
            binding.etNameSupplierEditSup.setText(result.name)
            binding.etAddresEditSup.setText(result.address)
            binding.etTelEditSup.setText(result.phone)

            if (result.imageSupplier.isNotEmpty()) {
                val decodedBytes = Base64.decode(result.imageSupplier, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                binding.ivSelectImageEditSup.setImageBitmap(bitmap)

                // 🔁 IMPORTANTE: también guardas esta imagen como actual
                imageSupplier = result.imageSupplier
            }

        }

        viewModel.editSupplierModel.observe(viewLifecycleOwner) { result ->
            if (result.name.isNotEmpty()){
                requireActivity().onBackPressed()
            }
        }

    }

    private fun initView() {
        binding.ivSelectImageEditSup.setOnClickListener {
            imagePickerMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnDeleteImageEditSup.setOnClickListener {
            binding.ivSelectImageEditSup.setImageResource(R.drawable.gallery)
            imageSupplier = null
        }

        binding.btnReturnEditSupplier.setOnClickListener {
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

        binding.btnRegisterEditSupp.setOnClickListener {
            if (validationForm()){
                editSupplier()
            }
        }
    }

    private fun validationForm(): Boolean {
        val etEmpty = "El campo no puede ser vacio"
        val regex = Regex("^\\d{10}$")
        if (binding.etNameSupplierEditSup.text.toString().isEmpty()){
            binding.etNameSupplierEditSup.error = etEmpty
            return false
        }else if (binding.etNameSupplierEditSup.text.toString().length <= 2){
            binding.etNameSupplierEditSup.error = "El nombre debe de ser mas de 2 caracteres"
            return false
        }else if (binding.etAddresEditSup.text.toString().isEmpty()){
            binding.etAddresEditSup.error = etEmpty
            return false
        }else if (binding.etAddresEditSup.text.toString().length <= 2){
            binding.etAddresEditSup.error = "La dirección debe de ser mas de 2 caracteres"
            return false
        }else if (binding.etTelEditSup.text.toString().isEmpty()){
            binding.etTelEditSup.error = etEmpty
            return false
        }else if (!regex.matches(binding.etTelEditSup.text.toString())){
            binding.etTelEditSup.error = "El numero de telefono debe de ser 10 digitos"
            return false
        }

        binding.etNameSupplierEditSup.error = null
        binding.etAddresEditSup.error = null
        binding.etTelEditSup.error = null

        return true

    }

    private fun editSupplier() {
        val supplier = SuppliersModel(
            supplierID,
            binding.etNameSupplierEditSup.text.toString(),
            binding.etTelEditSup.text.toString(),
            binding.etAddresEditSup.text.toString(),
            createdDate,
            imageSupplier ?: ""
        )

        //binding.pgresbar
        viewModel.onCreate(supplier)
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
        if (context is OnSupplierFragmentChangeListener) {
            listener = context
            listener?.onSupplierFragmentChangeListener(this)
        } else {
            throw RuntimeException("$context must implement OnFragmentChangedListener")
        }
    }

}