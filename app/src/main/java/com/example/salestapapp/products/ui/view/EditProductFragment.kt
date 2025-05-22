package com.example.salestapapp.products.ui.view

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.salestapapp.R
import com.example.salestapapp.databinding.FragmentEditProductBinding
import com.example.salestapapp.products.data.ProductsRepository
import com.example.salestapapp.products.data.domain.EditProductUseCase
import com.example.salestapapp.products.data.domain.GetProductByIdUseCase
import com.example.salestapapp.products.data.model.ProductModel
import com.example.salestapapp.products.ui.viewmodel.EditProductViewModel
import com.example.salestapapp.products.ui.viewmodel.EditProductViewModelFactory
import com.example.salestapapp.rom.CyberCoffeAppDatabase
import com.example.salestapapp.rom.CyberCoffeDatabase
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditProductFragment : Fragment() {

    private lateinit var _binding: FragmentEditProductBinding
    private val binding get() = _binding!!
    private lateinit var db: CyberCoffeDatabase
    private lateinit var viewModel: EditProductViewModel
    private var supplier = ""
    private var category = ""
    private var measurement = ""
    private var imageProduct:String? = ""
    private var listener: OnFragmentChangedListener? = null
    private var productID: Int = 0

    val imagePickerMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri ->

        if (uri != null){
            //hay imagen
            binding.ivSelectImageEditProd.setImageURI(uri)
            imageProduct = convertImageToByteArray(uri)
        }else{
            //no hay imagen
            Toast.makeText(requireContext(), "No se selecciono una imagen", Toast.LENGTH_SHORT).show()
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
        /*NO SE ESCONDE EL FLOATING BUTTON*/
        _binding = FragmentEditProductBinding.inflate(inflater, container, false)
        val repository: ProductsRepository = ProductsRepository(db)
        val viewModelProviderFactory = EditProductViewModelFactory(EditProductUseCase(repository),
            GetProductByIdUseCase(repository))
        viewModel = ViewModelProvider(
            this,
            viewModelProviderFactory
        )[EditProductViewModel::class.java]
            return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSpinnerSupplier()
        initSpinnerMeasurement()
        initSpinnerCategory()
        initViews()
        productID = arguments?.getInt("productID") ?: return
        viewModel.getProduct(productID)
        Log.e("AQUI EDIT", productID.toString())
        viewModel.productModel.observe(viewLifecycleOwner) { result ->
            Log.e("AQUI EDIT", result.id.toString())
            binding.etProductNameEditProd.setText(result.name)
            binding.etQuantityEditProd.setText("${result.quantity}")
            binding.etUnitPricesEditProd.setText("${result.price}")

            // Seleccionar item correspondiente en el Spinner de proveedor
            val supplierIndex = resources.getStringArray(R.array.supplier_array).indexOf(result.supplier)
            if (supplierIndex >= 0) binding.spSupplierEditProd.setSelection(supplierIndex)

// Categoría
            val categoryIndex = resources.getStringArray(R.array.category_array).indexOf(result.category)
            if (categoryIndex >= 0) binding.spCategoryEditProd.setSelection(categoryIndex)

// Unidad de medida
            val unitIndex = resources.getStringArray(R.array.unitProduct).indexOf(result.measurement)
            if (unitIndex >= 0) binding.spEditUnitMensurement.setSelection(unitIndex)

        }

        viewModel.editProdModel.observe(viewLifecycleOwner) { result ->
            // Manejar el resultado aquí
            Log.e("SE EDIT", "" + result.name)
            Log.e("SE EDIT", "" + result.category)
            Log.e("SE EDIT", "" + result.supplier)
            Log.e("SE EDIT", "" + result.createDate)
            if (result.name.isNotEmpty()){
                requireActivity().supportFragmentManager.popBackStack();
            }
        }

    }

    private fun initSpinnerSupplier() {
        var items = resources.getStringArray(R.array.supplier_array)
        items[0] = "Selecciona un Proovedor"
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spSupplierEditProd.adapter = adapter

        binding.spSupplierEditProd.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = items[position]
                supplier = selectedItem.toString()
                if (supplier != "Selecciona un Proovedor"){
                    binding.spCategoryEditProd.visibility = View.VISIBLE
                }else{
                    binding.spCategoryEditProd.visibility = View.GONE
                }
                /*val selectedId = selectedItem.id
                val selectedValue = selectedItem.value*/
                // Haz lo que necesites con el ID y el valor seleccionados
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Manejar el caso en que no se ha seleccionado nada
            }
        }

    }

    private fun initViews () {

        binding.btnReturnEditProduct.setOnClickListener {
            requireActivity().onBackPressed()
        }
        //selec Image
        binding.ivSelectImageEditProd.setOnClickListener {
            imagePickerMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnEditDeleteImage.setOnClickListener {
            binding.ivSelectImageEditProd.setImageResource(R.drawable.gallery)
            imageProduct = null
        }

        //btn quantity minus
        binding.btnMinusQuantityEditProd.setOnClickListener {
            var quantityTemp = binding.etQuantityEditProd.text.toString()
            if (quantityTemp.toInt() > 0){
                val newQuantity:Int = quantityTemp.toInt()-1
                binding.etQuantityEditProd.setText(newQuantity.toString())
            }
        }

        //btn add Quantoty
        binding.btnAddQuantityEditProd.setOnClickListener {
            var quantityTemp = binding.etQuantityEditProd.text.toString()
            if (quantityTemp.toInt() >= 0){
                val newQuantity:Int = quantityTemp.toInt()+1
                binding.etQuantityEditProd.setText(newQuantity.toString())
            }
        }

        binding.btnEditProduct.setOnClickListener {
            if (validationsForm()) {
                editProduct()
            }
        }
    }

    private fun editProduct() {
        val dateCreate = Date()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

        val product = ProductModel(
            productID,
            binding.etProductNameEditProd.text.toString(),
            binding.etQuantityEditProd.text.toString().toFloat(),
            binding.etUnitPricesEditProd.text.toString().toDouble(),
            imageProduct ?: "",
            2,
            category,
            1,
            supplier,
            2,
            measurement,
            dateFormat.format(dateCreate)
        )
        binding.pgSaveEditProduct.visibility = View.VISIBLE
        viewModel.onCreate(product)
    }

    private fun initSpinnerCategory() {
        var items = resources.getStringArray(R.array.category_array)
        items[0] = "Selecciona una Categoria"
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spCategoryEditProd.adapter = adapter

        binding.spCategoryEditProd.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = items[position]
                category = selectedItem.toString()
                /*val selectedId = selectedItem.id
                val selectedValue = selectedItem.value*/
                // Haz lo que necesites con el ID y el valor seleccionados
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Manejar el caso en que no se ha seleccionado nada
            }
        }
    }

    private fun initSpinnerMeasurement() {
        var items = resources.getStringArray(R.array.unitProduct)
        items[0] = "Selecciona un Unidad"
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spEditUnitMensurement.adapter = adapter

        binding.spEditUnitMensurement.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = items[position]
                measurement = selectedItem.toString()
                /*val selectedId = selectedItem.id
                val selectedValue = selectedItem.value*/
                // Haz lo que necesites con el ID y el valor seleccionados
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Manejar el caso en que no se ha seleccionado nada
            }
        }
    }

    private fun validationsForm(): Boolean {
        val etEmpty = "El campo no puede ser vacio"
        val unitPrice = binding.etUnitPricesEditProd.text.toString().toDouble()
        if (binding.etProductNameEditProd.text.toString().isEmpty()){
            binding.etProductNameEditProd.error = etEmpty
            return false
        }else if (binding.etProductNameEditProd.text.toString().length <= 2 ){
            binding.etProductNameEditProd.error = "El nombre no puede ser 2 caracteres"
            return false
        }else if (supplier == "Selecciona un Proovedor"){//DESPUES DE ESTRA VA EL DE LA IMAGEN
            binding.spEditSupplierError.visibility = View.VISIBLE
            return false
        }else if (category == "Selecciona una Categoria"){//DESPUES DE ESTRA VA EL DE LA IMAGEN
            binding.spEditCategoryError.visibility = View.VISIBLE
            return false
        }else if (measurement == "Selecciona un Unidad"){//DESPUES DE ESTRA VA EL DE LA IMAGEN
            binding.spEditUnitMensurementError.visibility = View.VISIBLE
            return false
        }else if (binding.etQuantityEditProd.text.toString().isEmpty() || binding.etQuantityEditProd.text.toString().toFloat() <= 0){
            binding.etQuantityEditProd.error = "La cantidad no puede ser vacio ó 0"
            return false
        }else if (binding.etUnitPricesEditProd.text.toString().isEmpty() || binding.etUnitPricesEditProd.text.toString().toDouble() <= 0.0 && unitPrice <= 0.9){
            binding.etUnitPricesEditProd.error = "El precio no puede ser vacio ó 0"
            return false
        }

        binding.etQuantityEditProd.error = null
        binding.spEditSupplierError.visibility = View.GONE
        binding.spEditCategoryError.visibility = View.GONE
        binding.spEditUnitMensurementError.visibility = View.GONE
        binding.etQuantityEditProd.error = null
        binding.etUnitPricesEditProd.error = null

        return true
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
        if (context is OnFragmentChangedListener) {
            listener = context
            listener?.onFragmentChanged(this)
        } else {
            throw RuntimeException("$context must implement OnFragmentChangedListener")
        }
    }

}