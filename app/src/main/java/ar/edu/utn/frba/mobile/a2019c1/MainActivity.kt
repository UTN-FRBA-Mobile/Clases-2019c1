package ar.edu.utn.frba.mobile.a2019c1

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import ar.edu.utn.frba.mobile.a2019c1.fragments.EditImageFragment
import ar.edu.utn.frba.mobile.a2019c1.fragments.ImagesFragment
import ar.edu.utn.frba.mobile.a2019c1.utils.permissions.Permissions
import ar.edu.utn.frba.mobile.a2019c1.utils.storage.preferences.MyPreferences
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ImagesFragment.ImagesFragmentInteractionListener, EditImageFragment.OnFragmentInteractionListener {
    init {
        System.loadLibrary("NativeImageProcessor")
    }

    private val pickImageIntentValue: Int = 1

    private var isEditingImage: Boolean = false
    private var launchImagePickerPending: Boolean = false
    private var showImagesListPending: Boolean = false

    private lateinit var imagesFragment: ImagesFragment
    private lateinit var editImageFragment: EditImageFragment
    private lateinit var actionSave: MenuItem
    private lateinit var actionCancel: MenuItem
    private lateinit var actionToggleViewGrid: MenuItem
    private lateinit var actionToggleViewList: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ar.edu.utn.frba.mobile.a2019c1.R.layout.activity_main)

        setSupportActionBar(toolbar)

        initLayout()

        add.setOnClickListener {
            Permissions.checkForPermissions(this,  android.Manifest.permission.WRITE_EXTERNAL_STORAGE, "La aplicación necesita acceso a su galería para poder editar fotos y guardar las ediciones.", object : Permissions.Callback {
                override fun onRequestSent() {
                    launchImagePickerPending = true
                }

                override fun onPermissionAlreadyGranted() {
                    launchImagePicker()
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        actionSave = menu.findItem(R.id.action_save)
        actionCancel = menu.findItem(R.id.action_cancel)
        actionToggleViewGrid = menu.findItem(R.id.action_toggle_view_grid)
        actionToggleViewList = menu.findItem(R.id.action_toggle_view_list)

        initMenuActions()

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_cancel) {
            initLayout()
            return true
        }

        if (id == R.id.action_save) {
            editImageFragment.save()
            initLayout()
            return true
        }

        if (id == R.id.action_toggle_view_list) {
            MyPreferences.setGridImagesListPreferredView(this, false)
            if(::imagesFragment.isInitialized) imagesFragment.changeViewToList()
            initMenuActions()
            return true
        }

        if (id == R.id.action_toggle_view_grid) {
            MyPreferences.setGridImagesListPreferredView(this, true)
            if(::imagesFragment.isInitialized) imagesFragment.changeViewToGrid()
            initMenuActions()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (isEditingImage) {
            initLayout()
        } else {
            super.onBackPressed()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            Permissions.REQUEST_WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.count() > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(launchImagePickerPending) {
                        launchImagePicker()
                    }
                    if(showImagesListPending) {
                        showImagesFragment()
                    }
                } else {
                    launchImagePickerPending = false
                    showImagesListPending = false
                }
                return
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == pickImageIntentValue && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data ?: return

            launchImageEdition(imageUri)

            return
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onImageClick(imageUri: Uri) {
        launchImageEdition(imageUri)
    }

    private fun initLayout() {
        initMenuActions()
        add.show()
        Permissions.checkForPermissions(this,  android.Manifest.permission.WRITE_EXTERNAL_STORAGE, "La aplicación necesita acceso a su galería para poder mostrar las fotos previamente editadas.", object : Permissions.Callback {
            override fun onRequestSent() {
                showImagesListPending = true
            }

            override fun onPermissionAlreadyGranted() {
                showImagesFragment()
            }
        })
    }

    private fun initMenuActions() {
        if(::actionSave.isInitialized){
            actionSave.isVisible = false
            actionCancel.isVisible = false
            if(MyPreferences.isGridImagesListPreferredView(this)){
                actionToggleViewList.isVisible = true
                actionToggleViewGrid.isVisible = false
            } else {
                actionToggleViewList.isVisible = false
                actionToggleViewGrid.isVisible = true
            }
        }
    }

    private fun showImagesFragment() {
        imagesFragment = ImagesFragment.newInstance(MyPreferences.isGridImagesListPreferredView(this))
        isEditingImage = false
        if(::editImageFragment.isInitialized){
            supportFragmentManager.beginTransaction().remove(editImageFragment)
                .replace(R.id.fragment_container, imagesFragment)
                .commit()
        } else {
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, imagesFragment).commit()
        }
    }

    private fun launchImagePicker() {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        startActivityForResult(chooserIntent, pickImageIntentValue)
    }

    private fun launchImageEdition(imageUri: Uri) {
        isEditingImage = true

        actionSave.isVisible = true
        actionCancel.isVisible = true
        actionToggleViewGrid.isVisible = false
        actionToggleViewList.isVisible = false

        add.hide()

        editImageFragment = EditImageFragment.newInstance(imageUri)
        //commitAllowingStateLoss en vez de commit ya que el onActivityResult se ejecuta después del onSaveInstanceState (nos vamos a otra app a elegir la imagen y después volvemos) - Como el estado del activity no nos importa, podemos hacer uso de commitAllowingStateLoss.
        if(::imagesFragment.isInitialized){
            supportFragmentManager.beginTransaction().remove(imagesFragment).replace(R.id.fragment_container, editImageFragment).commitAllowingStateLoss()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, editImageFragment).commitAllowingStateLoss()
        }
    }
}
