package ru.innohelpers.innohelp.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import ru.innohelpers.innohelp.R
import ru.innohelpers.innohelp.activity.LoginActivity
import ru.innohelpers.innohelp.activity.MainActivity
import ru.innohelpers.innohelp.view_data.user.UserViewData
import java.io.ByteArrayOutputStream


class ProfileFragment : Fragment() {

    private lateinit var userNameTextView: TextView
    private lateinit var pictureImageView: ImageView
    private lateinit var phoneEditText: TextInputEditText
    private lateinit var takenOrdersButton: AppCompatButton
    private lateinit var ratingTextView: TextView
    private lateinit var takenDeliveriesButton: AppCompatButton
    private lateinit var takenCaresButton: AppCompatButton
    private lateinit var content: LinearLayout
    private lateinit var loginButton: AppCompatButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)

        userNameTextView = view.findViewById(R.id.fragment_profile_user_name)
        pictureImageView = view.findViewById(R.id.fragment_profile_picture)
        phoneEditText = view.findViewById(R.id.fragment_profile_phone)
        ratingTextView = view.findViewById(R.id.fragment_profile_rating)
        takenOrdersButton = view.findViewById(R.id.fragment_profile_taken_orders)
        takenDeliveriesButton = view.findViewById(R.id.fragment_profile_taken_deliveries)
        takenCaresButton = view.findViewById(R.id.fragment_profile_taken_cares)
        content = view.findViewById(R.id.fragment_profile_content)
        loginButton = view.findViewById(R.id.fragment_profile_login)

        val viewModel = (activity as MainActivity).profileViewModel

        viewModel.currentUser.observe(
            viewLifecycleOwner,
            { _: UserViewData?, value: UserViewData? ->
                if (value == null) return@observe
                setUserData(value)
            })
        viewModel.isAuthorized.observe(viewLifecycleOwner, true, { _: Boolean?, _: Boolean? ->
            content.visibility = View.VISIBLE
            loginButton.visibility = View.GONE
        })
        viewModel.isAuthorized.observe(viewLifecycleOwner, false, { _: Boolean?, _: Boolean? ->
            content.visibility = View.GONE
            loginButton.visibility = View.VISIBLE
        })

        pictureImageView.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select picture"), 1)
        }

        loginButton.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        viewModel.loadData()
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null) return
            val iStream = requireContext().contentResolver.openInputStream(data.data!!)
            val bitmap = BitmapFactory.decodeStream(iStream)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
            val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
            val viewModel = (activity as MainActivity).profileViewModel
            viewModel.update(encoded, null)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).profileViewModel.loadData(true)
    }

    private fun setUserData(data: UserViewData) {
        userNameTextView.text = data.userName
        phoneEditText.setText(data.contactPhone ?: "")
        ratingTextView.text = data.rating.toString()

        Glide.with(requireContext()).load("data:image/png;base64," + data.profilePhoto).into(pictureImageView)
    }
}