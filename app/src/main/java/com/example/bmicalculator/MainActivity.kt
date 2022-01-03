package com.example.bmicalculator
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import android.support.v7.app.AppCompatActivity
class MainActivity : AppCompatActivity() {

    private val LICENSES_FILE_PATH: String = "file:///android_asset/licenses.html"

    companion object {
        private val CATEGORY_CODE_MAP: HashMap<Int, String> = HashMap(5)

        init {
            CATEGORY_CODE_MAP.put(R.color.blue, "Underweight")
            CATEGORY_CODE_MAP.put(R.color.green, "Healthy")
            CATEGORY_CODE_MAP.put(R.color.yellow, "Overweight")
            CATEGORY_CODE_MAP.put(R.color.orange, "Obese")
            CATEGORY_CODE_MAP.put(R.color.red, "Extremely Obese")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sbWeight.setOnProgressChangeListener(BMIProgressChangeListener(tvWeight, getString(R.string.unit_kg)))
        sbHeight.setOnProgressChangeListener(BMIProgressChangeListener(tvHeight, getString(R.string.unit_cm)))
    }

    fun calculate(v: View) {
        val weight = sbWeight.progress.toDouble()
        val height = sbHeight.progress.toDouble() / 100

        // Calculate bmi
        val bmiDetails = BMIUtils.calculateBMI(weight, height)

        // Update the TextView content
        tvBMI.text = bmiDetails.first

        // Fetch the category identifier color code
        val categoryIdentifierColorCode: Int = BMIUtils.getCategoryIdentifier(bmiDetails.second)

        // Update Category TextView's text and background
        updateViews(CATEGORY_CODE_MAP.get(categoryIdentifierColorCode), categoryIdentifierColorCode)
    }

    private fun updateViews(category: String?, colorCode: Int) {
        tvResult.text = category; //
        tvResult.setTextColor(ContextCompat.getColor(this, colorCode))
        (tvBMI.background as GradientDrawable).setColor(ContextCompat.getColor(this, colorCode))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_license) {
            val wv = WebView(this)
            wv.loadUrl(LICENSES_FILE_PATH)
            val builder = AlertDialog.Builder(this)
                .setTitle(getString(R.string.licences))
                .setView(wv)
                .setPositiveButton(getString(R.string.ok)) { dialog, which -> dialog.dismiss() }
            builder.show()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Inner class for monitoring changes in seek bar progress
     */
    inner class BMIProgressChangeListener(var tv: TextView, var unit: String) : DiscreteSeekBar.OnProgressChangeListener {

        override fun onProgressChanged(seekBar: DiscreteSeekBar?, value: Int, fromUser: Boolean) {
            tv.text = this@MainActivity.getString(R.string.bmi_value_formatted, value, unit)
        }

        override fun onStartTrackingTouch(seekBar: DiscreteSeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: DiscreteSeekBar?) {

        }
    }

}
