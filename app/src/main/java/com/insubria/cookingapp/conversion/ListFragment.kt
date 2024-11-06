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
import com.insubria.cookingapp.R
import com.insubria.cookingapp.utils.RecipeConversion
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

    private val recipeConversion = RecipeConversion()
    private val handler = Handler(Looper.getMainLooper())
    private val delayMillis: Long = 1000
    private var isUpdating = false  // Flag per evitare loop continui
    private val decimalFormat = DecimalFormat("#.##")  // DecimalFormat per arrotondare a due decimali

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

        // Aggiungi TextWatcher direttamente agli EditText
        addDebouncedTextWatcher(inputBurro, inputOlio) { value -> recipeConversion.butterToOil(value) }
        addDebouncedTextWatcher(inputOlio, inputBurro) { value -> recipeConversion.oilToButter(value) }
        addDebouncedTextWatcher(inputBurroRicotta, inputRicotta) { value -> recipeConversion.butterToRicotta(value) }
        addDebouncedTextWatcher(inputRicotta, inputBurroRicotta) { value -> recipeConversion.ricottaToButter(value) }
        addDebouncedTextWatcher(inputCucchiaino, inputGrammi) { value -> recipeConversion.spoonToGrams(value) }
        addDebouncedTextWatcher(inputGrammi, inputCucchiaino) { value -> recipeConversion.gramsToSpoons(value) }
        addDebouncedTextWatcher(inputBicchiere, inputCentilitri) { value -> recipeConversion.glassToCentiliters(value) }
        addDebouncedTextWatcher(inputCentilitri, inputBicchiere) { value -> recipeConversion.centilitersToGlass(value) }
        addDebouncedTextWatcher(inputZucchero, inputMiele) { value -> recipeConversion.sugarToHoney(value) }
        addDebouncedTextWatcher(inputMiele, inputZucchero) { value -> recipeConversion.honeyToSugar(value) }

        return view
    }

    // Aggiunge un TextWatcher con debounce direttamente all'EditText
    private fun addDebouncedTextWatcher(
        input: EditText,
        output: EditText,
        conversionFunction: (Double) -> Double
    ) {
        input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (isUpdating || s.isNullOrEmpty()) return

                handler.removeCallbacksAndMessages(input)

                handler.postDelayed({
                    val inputValue = s.toString().toDoubleOrNull()

                    if (inputValue != null) {
                        isUpdating = true

                        // Calcola il valore di conversione
                        val outputValue = conversionFunction(inputValue)

                        // Arrotonda il valore a due cifre decimali
                        val formattedValue = decimalFormat.format(outputValue)

                        // Imposta il valore nell'output EditText
                        output.setText(formattedValue)

                        isUpdating = false
                    } else {
                        output.setText("")
                    }
                }, delayMillis)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
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
