package lavanya.influxtask.util.glide

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class RoundedCornerTransformation(val radius: Float, val margin: Float, val diameter: Float = radius * 2): BitmapTransformation(){

    private val ID = "chandana.influxandroidtask.util.glide.RoundedCornerTransformation.$1.0"


    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((radius + diameter + margin).toByte())
    }

    override fun hashCode(): Int {
        return (ID.hashCode() + radius * 10000 + diameter * 1000 + margin * 100).toInt()
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val width = toTransform.width
        val height = toTransform.height

        val bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setHasAlpha(true)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        drawRoundRect(canvas, paint, width, height)
        return bitmap
    }

    private fun drawRoundRect(canvas: Canvas, paint: Paint, width: Int, height: Int) {
        val right = width - margin
        val bottom = height - margin

        canvas.drawRoundRect(RectF(margin, margin, right, margin + diameter), radius, radius,
                paint)
        canvas.drawRect(RectF(margin, margin + radius, right, bottom), paint)
    }

}