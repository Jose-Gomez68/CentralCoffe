package com.example.salestapapp.supplier.view

import android.content.ContentResolver
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
import com.example.salestapapp.R
import com.example.salestapapp.databinding.FragmentEditSupplierBinding
import com.example.salestapapp.rom.CyberCoffeAppDatabase
import com.example.salestapapp.rom.CyberCoffeDatabase
import java.io.ByteArrayOutputStream
import java.io.InputStream


class EditSupplierFragment : Fragment() {

    private var _binding: FragmentEditSupplierBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: CyberCoffeDatabase
    private var imageSupplier: String? = ""

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

        // Inflate the layout for this fragment
        return binding.root
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
            requireActivity().onBackPressed()
        }

        binding.btnRegisterEditSupp.setOnClickListener {
            if (validationForm()){

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
        }else if (!regex.matches(binding.etAddresEditSup.text.toString())){
            binding.etAddresEditSup.error = "El numero de telefono debe de ser 10 digitos"
            return false
        }

        binding.etNameSupplierEditSup.error = null
        binding.etAddresEditSup.error = null
        binding.etTelEditSup.error = null

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

}