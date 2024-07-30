package com.example.salestapapp.supplier.view

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.lifecycle.ViewModelProvider
import androidx.activity.result.contract.ActivityResultContracts.*
import com.example.salestapapp.R
import com.example.salestapapp.databinding.FragmentNewSupplierBinding
import com.example.salestapapp.rom.CyberCoffeAppDatabase
import com.example.salestapapp.rom.CyberCoffeDatabase
import com.example.salestapapp.supplier.data.SupplierRepository
import com.example.salestapapp.supplier.data.domain.SaveSupplierUseCase
import com.example.salestapapp.supplier.data.model.SupplierBundleModel
import com.example.salestapapp.supplier.data.model.SuppliersModel
import com.example.salestapapp.supplier.data.viewmodel.NewSupplierViewModel
import com.example.salestapapp.supplier.data.viewmodel.NewSupplierViewModelFactory
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.Date
import java.util.Locale

class NewSupplierFragment : Fragment() {

    private lateinit var binding: FragmentNewSupplierBinding
    private var listener: OnSupplierFragmentChangeListener? = null
    private var supplier: SuppliersModel? = null
    private lateinit var viewModel: NewSupplierViewModel
    private lateinit var db: CyberCoffeDatabase
    private var imageSupplier: String? = ""

    companion object {
        private const val ARG_SUPPLIER = "supplier"
        fun newInstance(supplier: SupplierBundleModel): NewSupplierFragment {
            val fragment = NewSupplierFragment()
            val args = Bundle()
            args.putSerializable(ARG_SUPPLIER, supplier)
            fragment.arguments = args
            return fragment
        }
    }

    val imagePickerMedia = registerForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.ivSelectImageNewSup.setImageURI(uri)
            imageSupplier = convertImageToByteArray(uri)
        }else{
            Toast.makeText(requireContext(), "No se selecciono una imagen", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = CyberCoffeAppDatabase.CyberCoffeAppDatabase.getInstance(requireContext())
        supplier = arguments?.getSerializable(ARG_SUPPLIER) as? SuppliersModel
        //if para ssaber si voy a editar o crear un supplier nuevo para reutilizar la vista
        if (supplier != null && supplier!!.name.isNotEmpty() && supplier!!.phone.isNotEmpty()) {
            // El objeto supplier contiene información
            //mostrar si es un item editable
        } else {
            // El objeto supplier es nulo o está vacío
            //saber si es que se creara un supplier nuevo
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val repository = SupplierRepository(db)
        val viewModelFactory = NewSupplierViewModelFactory(SaveSupplierUseCase(repository))
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[NewSupplierViewModel::class.java]

        binding = FragmentNewSupplierBinding.inflate(inflater, container,false)

        initView()

        binding.btnReturnNewSupplier.setOnClickListener {
            requireActivity().onBackPressed()
        }

        viewModel.newSupplierModel.observe(viewLifecycleOwner) { result ->
            if (result.name.isNotEmpty()){
                binding.etNameSupplierNewSup.setText("")
                binding.etAddresNewSup.setText("")
                binding.etTelNewSup.setText("")
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    /*listener of the fragments to
    FloatingButton gone or visible*/
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSupplierFragmentChangeListener) {
            listener = context
            listener?.onSupplierFragmentChangeListener(this)
        } else {
            throw RuntimeException("$context must implement OnFragmentChangedListener")
        }
    }

    private fun initView() {
        binding.ivSelectImageNewSup.setOnClickListener {
            imagePickerMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }
        binding.btnDeleteImageNewSup.setOnClickListener {
            binding.ivSelectImageNewSup.setImageResource(R.drawable.gallery)
            imageSupplier = null
        }
        binding.btnRegisterNewSupp.setOnClickListener {
            saveSupplier()
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
        if (binding.etNameSupplierNewSup.text.toString().isEmpty()){
            binding.etNameSupplierNewSup.error = etEmpty
            return false
        }else if (binding.etNameSupplierNewSup.text.toString().length <= 2){
            binding.etNameSupplierNewSup.error = "El nombre debe de ser mas de 2 caracteres"
            return false
        }else if (binding.etAddresNewSup.text.toString().isEmpty()){
            binding.etAddresNewSup.error = etEmpty
            return false
        }else if (binding.etAddresNewSup.text.toString().length <= 2){
            binding.etAddresNewSup.error = "La dirección debe de ser mas de 2 caracteres"
            return false
        }else if (binding.etTelNewSup.text.toString().isEmpty()){
            binding.etTelNewSup.error = etEmpty
            return false
        }else if (!regex.matches(binding.etAddresNewSup.text.toString())){
            binding.etAddresNewSup.error = "El numero de telefono debe de ser 10 digitos"
            return false
        }

        binding.etNameSupplierNewSup.error = null
        binding.etAddresNewSup.error = null
        binding.etTelNewSup.error = null

        return true

    }

    private fun saveSupplier() {

        if (validationForm()){
            val dateCreate = Date()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            val supplier = SuppliersModel(
                0,
                binding.etNameSupplierNewSup.text.toString(),
                binding.etTelNewSup.text.toString(),
                binding.etAddresNewSup.text.toString(),
                dateFormat.format(dateCreate),
                imageSupplier ?: ""
            )
            viewModel.onCreate(supplier)
        }

    }

}