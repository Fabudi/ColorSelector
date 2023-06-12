package inc.fabudi.colorselector

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class ColorSelector @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(
    context, attrs, defStyleAttr
) {

    private var prevColorBar: ProgressBar
    private var nextColorBar: ProgressBar
    private var currentColorBar: ProgressBar
    private var prevButton: ImageButton
    private var nextButton: ImageButton
    private var colorCodeLabel: TextView
    private var colors: ArrayList<Int> = ArrayList()
    private var currentColorIndex: Int

    init {
        inflate(context, R.layout.color_selector, this)
        prevColorBar = findViewById(R.id.prev_color)
        nextColorBar = findViewById(R.id.next_color)
        currentColorBar = findViewById(R.id.current_color)
        prevButton = findViewById(R.id.prev_color_button)
        nextButton = findViewById(R.id.next_color_button)
        colorCodeLabel = findViewById(R.id.color_code)
        nextButton.setOnClickListener { switchColorForward() }
        prevButton.setOnClickListener { switchColorBackward() }
        context.theme.obtainStyledAttributes(attrs, R.styleable.ColorSelector, 0, 0).apply {
            colors.add(getColor(R.styleable.ColorSelector_color_0, Color.BLACK))
            colors.add(getColor(R.styleable.ColorSelector_color_1, Color.BLACK))
            colors.add(getColor(R.styleable.ColorSelector_color_2, Color.BLACK))
            colors.add(getColor(R.styleable.ColorSelector_color_3, Color.BLACK))
            colors.add(getColor(R.styleable.ColorSelector_color_4, Color.BLACK))
            colors.add(getColor(R.styleable.ColorSelector_color_5, Color.BLACK))
            colors.add(getColor(R.styleable.ColorSelector_color_6, Color.BLACK))
            colors.add(getColor(R.styleable.ColorSelector_color_7, Color.BLACK))
            colors.add(getColor(R.styleable.ColorSelector_color_8, Color.BLACK))
            colors.add(getColor(R.styleable.ColorSelector_color_9, Color.BLACK))
            recycle()
        }
        currentColorIndex = 1
        setColors()
    }

    private fun setColors() {
        prevColorBar.progressTintList = ColorStateList.valueOf(colors[currentColorIndex - 1])
        currentColorBar.progressTintList = ColorStateList.valueOf(colors[currentColorIndex])
        nextColorBar.progressTintList = ColorStateList.valueOf(colors[currentColorIndex + 1])
        colorCodeLabel.text = "#${Integer.toHexString(colors[currentColorIndex])}"
    }

    private fun switchColorBackward() {
        val nextColor = getColorFromArrayByIndex(currentColorIndex + 1)
        val beforePrevColor = getColorFromArrayByIndex(currentColorIndex - 2)
        val prevColor = getColorFromArrayByIndex(currentColorIndex - 1)
        val currentColor = getColorFromArrayByIndex(currentColorIndex)
        colorCodeLabel.text = "#${Integer.toHexString(currentColor)}"

        nextColorBar.progressBackgroundTintList = ColorStateList.valueOf(currentColor)
        nextColorBar.progress = 100
        nextColorBar.progressTintList = ColorStateList.valueOf(nextColor)

        prevColorBar.progressBackgroundTintList = ColorStateList.valueOf(prevColor)
        prevColorBar.progress = 0
        prevColorBar.progressTintList = ColorStateList.valueOf(beforePrevColor)

        currentColorBar.progressBackgroundTintList = ColorStateList.valueOf(currentColor)
        currentColorBar.progress = 0
        currentColorBar.progressTintList = ColorStateList.valueOf(prevColor)

        val animatorSet = AnimatorSet()
        val animator = ObjectAnimator.ofInt(nextColorBar, "progress", 100, 0).setDuration(400)
        animator.startDelay = 100
        val animator2 = ObjectAnimator.ofInt(prevColorBar, "progress", 0, 100).setDuration(500)
        animator2.startDelay = 400
        animatorSet.playTogether(
            animator,
            animator2,
            ObjectAnimator.ofInt(currentColorBar, "progress", 0, 100).setDuration(500)
        )
        animatorSet.start()
        currentColorIndex--
        if (currentColor == -1) currentColorIndex = 9
    }

    private fun getColorFromArrayByIndex(index: Int): Int {
        return colors[
                if (index < 0) (index % colors.size + colors.size) % colors.size
                else index % colors.size
        ]
    }

    private fun switchColorForward() {
        val nextColor = getColorFromArrayByIndex(currentColorIndex + 1)
        val afterNextColor = getColorFromArrayByIndex(currentColorIndex + 2)
        val prevColor = getColorFromArrayByIndex(currentColorIndex - 1)
        val currentColor = getColorFromArrayByIndex(currentColorIndex)
        colorCodeLabel.text = "#${Integer.toHexString(currentColor)}"
        nextColorBar.progressBackgroundTintList = ColorStateList.valueOf(nextColor)
        nextColorBar.progress = 0
        nextColorBar.progressTintList = ColorStateList.valueOf(afterNextColor)

        prevColorBar.progressBackgroundTintList = ColorStateList.valueOf(currentColor)
        prevColorBar.progress = 100
        prevColorBar.progressTintList = ColorStateList.valueOf(prevColor)

        currentColorBar.progressBackgroundTintList = ColorStateList.valueOf(nextColor)
        currentColorBar.progressTintList = ColorStateList.valueOf(currentColor)

        val animatorSet = AnimatorSet()
        val animator = ObjectAnimator.ofInt(nextColorBar, "progress", 0, 100).setDuration(400)
        animator.startDelay = 400
        val animator2 = ObjectAnimator.ofInt(prevColorBar, "progress", 100, 0).setDuration(500)
        animator2.startDelay = 100
        animatorSet.playTogether(
            animator,
            animator2,
            ObjectAnimator.ofInt(currentColorBar, "progress", 100, 0).setDuration(500)
        )
        animatorSet.start()
        currentColorIndex++
        if (currentColor == colors.size) currentColorIndex = 0
    }

}