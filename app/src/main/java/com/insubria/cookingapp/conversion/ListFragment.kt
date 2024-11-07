package com.insubria.cookingapp.conversion

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.insubria.cookingapp.R
import com.insubria.cookingapp.View.MainActivity
import com.insubria.cookingapp.utils.RecipeConversions
import java.text.DecimalFormat

class ListFragment : Fragment() {

    private lateinit var inputBurro: EditText
    private lateinit var inputOlio: EditText
    private lateinit var inputBurroRicotta: EditText
    private lateinit var inputRicotta: EditText
    private lateinit var inputCucchiaino: EditText
    private lateinit var inputGrammi: EditText
    private lateinit var inputBicchiere: EditText
    private lateinit var inputCentilitri: EditText
    private lateinit var inputZucchero: EditText
    private lateinit var inputMiele: EditText

    // Memorizziamo i TextWatcher
    private lateinit var burroWatcher: TextWatcher
    private lateinit var olioWatcher: TextWatcher
    private lateinit var burroRicottaWatcher: TextWatcher
    private lateinit var ricottaWatcher: TextWatcher
    private lateinit var cucchiainoWatcher: TextWatcher
    private lateinit var grammiWatcher: TextWatcher
    private lateinit var bicchiereWatcher: TextWatcher
    private lateinit var centilitriWatcher: TextWatcher
    private lateinit var zuccheroWatcher: TextWatcher
    private lateinit var mieleWatcher: TextWatcher

    private val recipeConversion = RecipeConversions()
    private val handler = Handler(Looper.getMainLooper())
    private val delayMillis: Long = 500
    private var isUpdating = false  // Flag per evitare loop continui

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        // Ottieni riferimenti agli EditText
        inputBurro = view.findViewById(R.id.input_burro)
        inputOlio = view.findViewById(R.id.input_olio)
        inputBurroRicotta = view.findViewById(R.id.input_burro_ricotta)
        inputRicotta = view.findViewById(R.id.input_ricotta)
        inputCucchiaino = view.findViewById(R.id.input_cucchiaino)
        inputGrammi = view.findViewById(R.id.input_grammi)
        inputBicchiere = view.findViewById(R.id.input_bicchiere)
        inputCentilitri = view.findViewById(R.id.input_centilitri)
        inputZucchero = view.findViewById(R.id.input_zucchero)
        inputMiele = view.findViewById(R.id.input_miele)

        // Aggiungi i TextWatcher e salvali nelle variabili
        burroWatcher = createTextWatcher(inputBurro, inputOlio, ::butterToOil)
        olioWatcher = createTextWatcher(inputOlio, inputBurro, ::oilToButter)
        burroRicottaWatcher = createTextWatcher(inputBurroRicotta, inputRicotta, ::butterToRicotta)
        ricottaWatcher = createTextWatcher(inputRicotta, inputBurroRicotta, ::ricottaToButter)
        cucchiainoWatcher = createTextWatcher(inputCucchiaino, inputGrammi, ::spoonToGrams)
        grammiWatcher = createTextWatcher(inputGrammi, inputCucchiaino, ::gramsToSpoons)
        bicchiereWatcher = createTextWatcher(inputBicchiere, inputCentilitri, ::glassToCentiliters)
        centilitriWatcher = createTextWatcher(inputCentilitri, inputBicchiere, ::centilitersToGlass)
        zuccheroWatcher = createTextWatcher(inputZucchero, inputMiele, ::sugarToHoney)
        mieleWatcher = createTextWatcher(inputMiele, inputZucchero, ::honeyToSugar)

        // Aggiungi i TextWatcher agli EditText
        inputBurro.addTextChangedListener(burroWatcher)
        inputOlio.addTextChangedListener(olioWatcher)
        inputBurroRicotta.addTextChangedListener(burroRicottaWatcher)
        inputRicotta.addTextChangedListener(ricottaWatcher)
        inputCucchiaino.addTextChangedListener(cucchiainoWatcher)
        inputGrammi.addTextChangedListener(grammiWatcher)
        inputBicchiere.addTextChangedListener(bicchiereWatcher)
        inputCentilitri.addTextChangedListener(centilitriWatcher)
        inputZucchero.addTextChangedListener(zuccheroWatcher)
        inputMiele.addTextChangedListener(mieleWatcher)

        return view
    }

    // Funzione per creare il TextWatcher con debounce
    private fun createTextWatcher(
        input: EditText,
        output: EditText,
        conversionFunction: (Double) -> Double
    ): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (isUpdating || s.isNullOrEmpty()) return  // Evita loop infiniti se l'update è in corso

                handler.removeCallbacksAndMessages(input)  // Rimuovi gli aggiornamenti precedenti

                handler.postDelayed({
                    val inputValue = s.toString().toDoubleOrNull()

                    if (inputValue != null) {
                        // Impedisce l'aggiornamento contemporaneo
                        isUpdating = true

                        // Disabilita temporaneamente tutti i TextWatcher
                        disableAllTextWatchers()

                        // Calcola il valore di conversione
                        val outputValue = conversionFunction(inputValue)

                        // Imposta il valore nel campo di output
                        output.setText(outputValue.toString())

                        // Riabilita i TextWatcher dopo l'aggiornamento
                        enableAllTextWatchers()

                        isUpdating = false
                    } else {
                        // Se il valore non è valido, pulisci l'output
                        output.setText("")
                    }
                }, delayMillis)  // Debounce con ritardo
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
    }

    // Funzione per disabilitare tutti i TextWatcher
    private fun disableAllTextWatchers() {
        inputBurro.removeTextChangedListener(burroWatcher)
        inputOlio.removeTextChangedListener(olioWatcher)
        inputBurroRicotta.removeTextChangedListener(burroRicottaWatcher)
        inputRicotta.removeTextChangedListener(ricottaWatcher)
        inputCucchiaino.removeTextChangedListener(cucchiainoWatcher)
        inputGrammi.removeTextChangedListener(grammiWatcher)
        inputBicchiere.removeTextChangedListener(bicchiereWatcher)
        inputCentilitri.removeTextChangedListener(centilitriWatcher)
        inputZucchero.removeTextChangedListener(zuccheroWatcher)
        inputMiele.removeTextChangedListener(mieleWatcher)
    }

    // Funzione per riabilitare tutti i TextWatcher
    private fun enableAllTextWatchers() {
        inputBurro.addTextChangedListener(burroWatcher)
        inputOlio.addTextChangedListener(olioWatcher)
        inputBurroRicotta.addTextChangedListener(burroRicottaWatcher)
        inputRicotta.addTextChangedListener(ricottaWatcher)
        inputCucchiaino.addTextChangedListener(cucchiainoWatcher)
        inputGrammi.addTextChangedListener(grammiWatcher)
        inputBicchiere.addTextChangedListener(bicchiereWatcher)
        inputCentilitri.addTextChangedListener(centilitriWatcher)
        inputZucchero.addTextChangedListener(zuccheroWatcher)
        inputMiele.addTextChangedListener(mieleWatcher)
    }

    // Funzioni di conversione
    private fun butterToOil(butterGrams: Double): Double {
        return recipeConversion.butterToOil(butterGrams)
    }

    private fun oilToButter(oilGrams: Double): Double {
        return recipeConversion.oilToButter(oilGrams)
    }

    private fun butterToRicotta(butterGrams: Double): Double {
        return recipeConversion.butterToRicotta(butterGrams)
    }

    private fun ricottaToButter(ricottaGrams: Double): Double {
        return recipeConversion.ricottaToButter(ricottaGrams)
    }

    private fun spoonToGrams(spoons: Double): Double {
        return recipeConversion.spoonToGrams(spoons)
    }

    private fun gramsToSpoons(grams: Double): Double {
        return recipeConversion.gramsToSpoons(grams)
    }

    private fun glassToCentiliters(glasses: Double): Double {
        return recipeConversion.glassToCentiliters(glasses)
    }

    private fun centilitersToGlass(centiliters: Double): Double {
        return recipeConversion.centilitersToGlass(centiliters)
    }

    private fun sugarToHoney(sugars: Double): Double {
        return recipeConversion.sugarToHoney(sugars)
    }

    private fun honeyToSugar(honeys: Double): Double {
        return recipeConversion.honeyToSugar(honeys)
    }
}
