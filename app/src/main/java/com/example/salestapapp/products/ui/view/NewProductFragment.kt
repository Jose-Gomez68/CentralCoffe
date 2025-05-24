package com.example.salestapapp.products.ui.view

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.salestapapp.R
import com.example.salestapapp.databinding.FragmentNewProductBinding
import com.example.salestapapp.products.data.ProductsRepository
import com.example.salestapapp.products.data.domain.InsertProductUseCase
import com.example.salestapapp.products.data.model.ProductModel
import com.example.salestapapp.products.ui.viewmodel.NewProductViewModel
import com.example.salestapapp.products.ui.viewmodel.NewProductViewModelFactory
import com.example.salestapapp.rom.CyberCoffeAppDatabase
import com.example.salestapapp.rom.CyberCoffeDatabase
import com.example.salestapapp.util.UtilsFunctions
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class NewProductFragment : Fragment() {

    private var _binding: FragmentNewProductBinding? = null

    // Acceso seguro al enlace de vista a través de esta propiedad
    private val binding get() = _binding!!
    private lateinit var db: CyberCoffeDatabase // Cambiamos a lateinit
    private lateinit var viewModel: NewProductViewModel
    private var supplier = ""
    private var category = ""
    private var measurement = ""
    private var imageProduct:String? = ""
    private var listener: OnFragmentChangedListener? = null
    private lateinit var utilsFunctions: UtilsFunctions

    val imagePickerMedia = registerForActivityResult(PickVisualMedia()){ uri ->

        if (uri != null){
            //hay imagen
            binding.ivSelectImageNewProd.setImageURI(uri)
            imageProduct = convertImageToByteArray(uri)
        }else{
            //no hay imagen
            Toast.makeText(requireContext(), "No se selecciono una imagen",Toast.LENGTH_SHORT).show()
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
        _binding = FragmentNewProductBinding.inflate(inflater, container, false)
        val repository: ProductsRepository = ProductsRepository(db)
        val viewModelProviderFactory = NewProductViewModelFactory(InsertProductUseCase(repository))
        viewModel = ViewModelProvider(
            this,
            viewModelProviderFactory
        )[NewProductViewModel::class.java]

        binding.btnReturnNewProd.setOnClickListener {
            utilsFunctions.showConfirmDialog(requireActivity(),
                "Estas Seguro de Salir?",
                "Una vez saliendo no podras recurar la informacion que escribiste.!!",
                "Salir",
                "Cancelar",
                onConfirm = {
                    requireActivity().onBackPressed()
                },
                onCancel = {

                }
            )
        }

        // Retornar la vista raíz del diseño inflado
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        utilsFunctions = UtilsFunctions()
        initSpinnerSupplier()
        //initSpinnerMeasurement()
        initSpinnerCategory()
        initViews()


        viewModel.newProdModel.observe(viewLifecycleOwner) { result ->
            // Manejar el resultado aquí
            Log.e("SE INSERTO", "" + result.name)
            Log.e("SE INSERTO", "" + result.category)
            Log.e("SE INSERTO", "" + result.supplier)
            Log.e("SE INSERTO", "" + result.createDate)
            if (result.name.isNotEmpty()){
                binding.pgSaveProduct.visibility = View.GONE
                binding.etProductNameNewProd.setText("")
                binding.etQuantityNewProd.setText("0")
                binding.etUnitPricesNewProd.setText("0.0")
                binding.spSupplierNewProd.setSelection(0)
                binding.spCategoryNewProd.setSelection(0)
                binding.spCategoryNewProd.visibility = View.GONE
                //binding.spUnitMensurement.setSelection(0)
                binding.ivSelectImageNewProd.setImageResource(R.drawable.gallery)
                imageProduct = null
            }
        }

        //asi se usa el room
        lifecycleScope.launch {
            // Ahora puedes trabajar con el TextView
            //textView.text = db.getProductsDao().getAllProducts().toString()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Limpiar la referencia al enlace de vista para evitar fugas de memoria
        _binding = null
    }

    private fun initViews () {
        //selec Image
        binding.ivSelectImageNewProd.setOnClickListener {
            imagePickerMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }

        binding.btnDeleteImage.setOnClickListener {
            binding.ivSelectImageNewProd.setImageResource(R.drawable.gallery)
            imageProduct = null
        }

        //btn quantity minus
        binding.btnMinusQuantityNewProd.setOnClickListener {
            var quantityTemp = binding.etQuantityNewProd.text.toString()
            if (quantityTemp.toInt() > 0){
                val newQuantity:Int = quantityTemp.toInt()-1
                binding.etQuantityNewProd.setText(newQuantity.toString())
            }
        }

        //btn add Quantoty
        binding.btnAddQuantityNewProd.setOnClickListener {
            var quantityTemp = binding.etQuantityNewProd.text.toString()
            if (quantityTemp.toInt() >= 0){
                val newQuantity:Int = quantityTemp.toInt()+1
                binding.etQuantityNewProd.setText(newQuantity.toString())
            }
        }

        binding.btnRegisterProduct.setOnClickListener {
            if (validationsForm()) {
                insertProduct()
            }
        }
    }

    private fun insertProduct() {

        val dateCreate = Date()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

        val product = ProductModel(
            0,
            binding.etProductNameNewProd.text.toString(),
            binding.etQuantityNewProd.text.toString().toInt(),
            binding.etUnitPricesNewProd.text.toString().toDouble(),
            imageProduct ?: "",
            1,
            category,
            1,
            supplier,
            dateFormat.format(dateCreate)
        )

        /*
        * val product = ProductModel(
            0,
            binding.etProductNameNewProd.text.toString(),
            binding.etQuantityNewProd.text.toString().toFloat(),
            binding.etUnitPricesNewProd.text.toString().toDouble(),
            imageProduct ?: "",
            1,
            category,
            1,
            supplier,
            1,
            measurement,
            dateFormat.format(dateCreate)
        )*/
        binding.pgSaveProduct.visibility = View.VISIBLE
        viewModel.onCreate(product)

    }

    private fun initSpinnerSupplier() {
        var items = resources.getStringArray(R.array.supplier_array)
        items[0] = "Selecciona un Proovedor"
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spSupplierNewProd.adapter = adapter

        binding.spSupplierNewProd.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = items[position]
                supplier = selectedItem.toString()
                if (supplier != "Selecciona un Proovedor"){
                    binding.spCategoryNewProd.visibility = View.VISIBLE
                }else{
                    binding.spCategoryNewProd.visibility = View.GONE
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

    private fun initSpinnerCategory() {
        var items = resources.getStringArray(R.array.category_array)
        items[0] = "Selecciona una Categoria"
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spCategoryNewProd.adapter = adapter

        binding.spCategoryNewProd.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

    /*private fun initSpinnerMeasurement() {
        var items = resources.getStringArray(R.array.unitProduct)
        items[0] = "Selecciona un Unidad"
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spUnitMensurement.adapter = adapter

        binding.spUnitMensurement.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = items[position]
                measurement = selectedItem.toString()
                *//*val selectedId = selectedItem.id
                val selectedValue = selectedItem.value*//*
                // Haz lo que necesites con el ID y el valor seleccionados
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Manejar el caso en que no se ha seleccionado nada
            }
        }
    }
*/
    private fun validationsForm(): Boolean {
        val etEmpty = "El campo no puede ser vacio"
        val unitPrice = binding.etUnitPricesNewProd.text.toString().toDouble()
        if (binding.etProductNameNewProd.text.toString().isEmpty()){
            binding.etProductNameNewProd.error = etEmpty
            return false
        }else if (binding.etProductNameNewProd.text.toString().length <= 2 ){
            binding.etProductNameNewProd.error = "El nombre no puede ser 2 caracteres"
            return false
        }else if (supplier == "Selecciona un Proovedor"){//DESPUES DE ESTRA VA EL DE LA IMAGEN
            binding.spSupplierError.visibility = View.VISIBLE
            return false
        }else if (category == "Selecciona una Categoria"){//DESPUES DE ESTRA VA EL DE LA IMAGEN
            binding.spCategoryError.visibility = View.VISIBLE
            return false
        }/*else if (measurement == "Selecciona un Unidad"){//DESPUES DE ESTRA VA EL DE LA IMAGEN
            binding.spUnitMensurementError.visibility = View.VISIBLE
            return false
        }*/else if (binding.etQuantityNewProd.text.toString().isEmpty() || binding.etQuantityNewProd.text.toString().toInt() <= 0){
            binding.etQuantityNewProd.error = "La cantidad no puede ser vacio ó 0"
            return false
        }else if (binding.etUnitPricesNewProd.text.toString().isEmpty() || binding.etUnitPricesNewProd.text.toString().toDouble() <= 0.0 && unitPrice <= 0.9){
            binding.etUnitPricesNewProd.error = "El precio no puede ser vacio ó 0"
            return false
        }

        binding.etProductNameNewProd.error = null
        binding.spSupplierError.visibility = View.GONE
        binding.spCategoryError.visibility = View.GONE
        //binding.spUnitMensurementError.visibility = View.GONE
        binding.etQuantityNewProd.error = null
        binding.etUnitPricesNewProd.error = null

        return true
    }

    //asi convierto la imagen a ByteArray cuando este en string en la db
    //val byteArray: ByteArray = Base64.decode(varDe la bd imagen, Base64.DEFAULT)
    //imageView.setImageBitmap(bitmap)
    private fun convertBase64ToBitmap(base64String: String): Bitmap? {
        val decodedByteArray: ByteArray = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
    }

    private fun convertImageToByteArray(uri:Uri): String? {
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