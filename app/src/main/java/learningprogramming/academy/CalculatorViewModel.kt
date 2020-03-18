package learningprogramming.academy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {

    // Variables to hold the operands and type of calculation
    private var operand1: Double? = null
    private var pendingOperation = "="
//remember not to expose MutableLiveData objects~!~!~!
    private val result = MutableLiveData<Double>()
    val stringResult: LiveData<String>
        get() = Transformations.map(result) { it.toString()}

    private val newNumber = MutableLiveData<String>()  //MutableLiveData has extends LiveData
    val stringNewNumber: LiveData<String>   //LiveData is abstract
            get() = newNumber

    private val operation = MutableLiveData<String>()
    val stringOperation: LiveData<String>
        get() = operation

    fun digitPressed( caption: String){
        if (newNumber.value != null) {
            newNumber.value = newNumber.value + caption
        } else {
            newNumber.value = caption
        }
    }

    fun operandPressed(op: String){
        try{
            val value = newNumber.value?.toDouble() //newNumber.text.toString().toDouble()
            if (value != null){
                performOperation(value, op)
            }
        } catch(e: NumberFormatException) {
            newNumber.value = "" //newNumber.setText("")
        }
        pendingOperation = op
        operation.value = pendingOperation //operation.text = pendingOperation
    }

    fun negPressed(){
        val value = newNumber.value //newNumber.text.toString()
        if (value == null || value.isEmpty()) {
            newNumber.value = "-"     // newNumber.setText("-")
        } else {
            try{
                var doubleValue = value.toDouble()
                doubleValue *= -1
                newNumber.value = doubleValue.toString() //newNumber.setText(doubleValue.toString())
            } catch (e: NumberFormatException){
                //newNumber was "-" or ".", so clear it
                newNumber.value = "" //newNumber.setText("")
            }
        }
    }

    fun clrPressed(){
        operand1 = null
        operation.value = "" //operation.text = ""
        //result.text.clear()
        //newNumber.text.clear()
    }


    private fun performOperation(value: Double, operation: String){
        if (operand1 == null) {  // check null then can use   !!
            operand1 = value
        } else {
            if (pendingOperation == "=") { // don't do this == with a string in Java. === same as == in Java
                pendingOperation = operation
            }

            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (value == 0.0) {
                    Double.NaN // handle attempt to divide by zero
                } else {
                    operand1!! / value
                }
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }
        }
        //result.value = operand1.toString()
        result.value = operand1
        newNumber.value = ""
    }
}