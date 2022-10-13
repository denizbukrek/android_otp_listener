
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.widget.Toast
import java.util.regex.Matcher
import java.util.regex.Pattern


class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.extras
        if (bundle != null) {
            // get sms objects
            val pdus = bundle["pdus"] as Array<*>?
            if (pdus!!.isEmpty()) {
                return
            }
            // large message might be broken into many
            val messages: Array<SmsMessage?> = arrayOfNulls<SmsMessage>(pdus.size)
            val sb = StringBuilder()
            for (i in pdus.indices) {
                messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                sb.append(messages[i]?.messageBody)
            }
            val sender: String? = messages[0]?.originatingAddress
            val message = sb.toString()

            val pattern: Pattern = Pattern.compile("(\\d{6})")

            //   \d is for a digit
            //   {} is the number of digits here 6.
            val matcher: Matcher = pattern.matcher(message)
            var otpCode = ""
            if (matcher.find()) {
                otpCode = matcher.group(0) // 4 digit number
            }
            Toast.makeText(context, "NUMBER: $sender OTP: $otpCode" , Toast.LENGTH_LONG).show()
            println(otpCode)
            println(message)
        }
    }
}