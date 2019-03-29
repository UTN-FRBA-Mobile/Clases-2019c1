package ar.edu.utn.frba.mobile.a2019c1.Login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import ar.edu.utn.frba.mobile.a2019c1.R
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders

class SignUpStepOneFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up_step_one, container, false)

        val nextButton = view.findViewById<Button>(R.id.next_button)
        nextButton.setOnClickListener {
            listener!!.onSignUpNextStep()
        }

        viewModel = ViewModelProviders.of(activity!!).get(SignUpViewModel::class.java!!)

        view.findViewById<EditText>(R.id.firstName).setText(viewModel.firstName)
        view.findViewById<EditText>(R.id.lastName).setText(viewModel.lastName)
        view.findViewById<EditText>(R.id.dni).setText(viewModel.dni)
        view.findViewById<EditText>(R.id.phoneNumer).setText(viewModel.phoneNumer)

        return view
    }

    override fun onPause() {
        viewModel.firstName = view?.findViewById<EditText>(R.id.firstName)?.text.toString()
        viewModel.lastName = view?.findViewById<EditText>(R.id.lastName)?.text.toString()
        viewModel.dni = view?.findViewById<EditText>(R.id.dni)?.text.toString()
        viewModel.phoneNumer = view?.findViewById<EditText>(R.id.phoneNumer)?.text.toString()

        super.onPause()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onSignUpNextStep()
    }
}
