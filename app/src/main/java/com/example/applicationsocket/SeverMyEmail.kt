package com.example.applicationsocket
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import com.example.applicationsocket.ViewModel.ProfileUser.BodyChangePass
import com.example.applicationsocket.ViewModel.Screen.generateOTP
import com.example.applicationsocket.data.UserIDModel
import java.util.Properties
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
class SeverMyEmail {
    fun sendEmail(to: String, subject: String, body: String, callback: (Boolean, String?) -> Unit) {
        val username = "dohuunhan321@gmail.com" // Thay bằng email của bạn
        val password = "qfko rcit slfg gpbu" // Thay bằng mật khẩu của bạn

        val props = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.ssl.enable", "true") // Sử dụng SSL
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.port", "465")
        }

        val session = Session.getInstance(props, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })

        try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(username))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
                this.subject = subject
                setText(body)
            }

            Thread {
                try {
                    Transport.send(message)
                    callback(true, null)
                } catch (e: Exception) {
                    e.printStackTrace()
                    callback(false, e.message)
                }
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
            callback(false, e.message)
        }
    }

}
//@Composable
fun sendEmail(userEmail:String,otp: String){
    val emailService = SeverMyEmail()
    val handler = Handler(Looper.getMainLooper())

    emailService.sendEmail(
        to = userEmail,
        subject = "Mã OTP Socket Application",
        body = "Đây là mã OTP để đổi Password của bạn: $otp"
    ) { success, errorMessage ->
        handler.post {
            if (success) {
//                Toast.makeText(context, "Gửi OTP Thành Công", Toast.LENGTH_LONG).show()
            } else {
                val errorText = errorMessage ?: "Đã xảy ra lỗi khi gửi OTP"
//                Toast.makeText(context, "Gửi OTP Thất Bại: $errorText", Toast.LENGTH_LONG).show()
            }
        }
    }
}
