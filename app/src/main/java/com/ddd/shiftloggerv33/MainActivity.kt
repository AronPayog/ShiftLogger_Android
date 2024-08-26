package com.ddd.shiftloggerv33


//---------------------------

//===========================
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {

    lateinit var handler: Handler;
//    var dbMangers = DatabaseManger(this)
    var auth = Authentication(this)

    var refresh: Int = 0;
    var HOUR: Int = 0;
    var MINUTE: Int = 0;
    var timeInHour: Int = 0
    var timeInMinute: Int = 0
    var timeOutHour: Int = 0
    var timeOutMinute: Int = 0
    var isTimeInClicked: Boolean = false;
    var isTimeOutClicked: Boolean = false;
    var isAmOrPm: String = ""
    var curentloginUser = ""
    var isCurrentLogin = false;


    val userRegInfoObject: MutableMap<String, MutableList<String>> = mutableMapOf()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        handler = Handler(Looper.getMainLooper())
        val gotomain: FrameLayout =  findViewById(R.id.startBtn);
        gotomain.setOnClickListener{main_home()}


    }

    fun main_home(){
        setContentView(R.layout.main_home)
        val timeinTime: TextView = findViewById(R.id.timeinTime);
        val timeOutTime: TextView = findViewById(R.id.timeoutTime);
        val logInUserDislplay: TextView = findViewById(R.id.loginUserNameDisplay)

        if(isCurrentLogin){
            logInUserDislplay.text = curentloginUser
        }else{
            logInUserDislplay.text = "Guest"
        }
        if(timeInHour != 0 && timeInMinute != 0){
            timeinTime.text = ""+(if(timeInHour >= 13) (timeInHour-12) else timeInHour)+":"+timeInMinute+" "+isAmOrPm
        }
        if(timeOutHour != 0 && timeOutMinute !=0){
            timeOutTime.text = ""+(if(timeOutHour >= 13) (timeOutHour-12) else timeOutHour)+":"+timeOutMinute+" "+isAmOrPm
        }


        val realTimeText: TextView = findViewById(R.id.realTime);
        runningclock(realTimeText);


        val exitBtn: FrameLayout = findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener{
            Toast.makeText(this@MainActivity, "Exit", Toast.LENGTH_SHORT).show();
            finish()
        }

        val loginBtn: FrameLayout = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener{
            Toast.makeText(this@MainActivity, "Login Page!", Toast.LENGTH_SHORT).show()
            Log.d("MyActivity", "This is Debugging!!")
            login_layout()
        }
        val timeInBtn: FrameLayout = findViewById(R.id.timeinBtn)
        timeInBtn.setOnClickListener{
            if(!isTimeInClicked){
                timeInHour = HOUR
                timeInMinute = MINUTE;
                timeinTime.text = ""+(if(timeInHour >= 13) (timeInHour-12) else timeInHour)+":"+timeInMinute+" "+isAmOrPm
                isTimeInClicked = true;
            }
        }
        val timeOutBtn: FrameLayout = findViewById(R.id.timeoutBtn)
        timeOutBtn.setOnClickListener{
            if(!isTimeOutClicked) {
                timeOutHour = HOUR;
                timeOutMinute = MINUTE;
                timeOutTime.text = "" + (if (timeOutHour >= 13) (timeOutHour - 12) else timeOutHour) + ":" + timeOutMinute+" "+isAmOrPm
                isTimeOutClicked = true;
            }
        }
        val saveBtn: FrameLayout = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener{
            isTimeInClicked = false
            isTimeOutClicked = false
//            dbMangers.insertData("Test", "0000-00-00",""+timeInHour+":"+timeInMinute, ""+timeOutHour+":"+timeOutMinute)

            Toast.makeText(this@MainActivity, "Saving Data.....", Toast.LENGTH_SHORT).show()
        }

        val seedataBtn: FrameLayout = findViewById(R.id.showdataBtn)
        seedataBtn.setOnClickListener{see_data()}

    }

    fun runningclock(Target: TextView){

        handler.post(object : Runnable {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun run() {
                refresh+=16
                val cdt = LocalDateTime.now()
                HOUR = cdt.hour;
                MINUTE = cdt.minute;

                isAmOrPm = if(HOUR >= 12) "PM" else "AM"
                Target.text = ""+(if(cdt.hour >= 13) (cdt.hour-12) else cdt.hour)+":"+cdt.minute+" "+isAmOrPm

                handler.postDelayed(this, 1000)
            }
        })

    }
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }


    private fun login_layout(){
        setContentView(R.layout.login_layout)
        val userNameInput: EditText = findViewById(R.id.usernameinput)
        val userPasswordInput: EditText = findViewById(R.id.userinputpassword)

        val displaytext: TextView = findViewById(R.id.userOutput)

        val loginBtn: FrameLayout = findViewById(R.id.loginBtn)
        loginBtn.setOnClickListener{
            val USERNAME = userNameInput.text
            val USERPASSWORD = userPasswordInput.text
            val (isAccountExists, UserFullName) = auth.authenticationUser(USERNAME.toString(), USERPASSWORD.toString())
            if(isAccountExists){
                displaytext.text = ""+UserFullName
                curentloginUser = ""+UserFullName
                isCurrentLogin = true;
                Toast.makeText(this, "Success Fully Login...",Toast.LENGTH_SHORT).show()
                main_home()
            }else{
                isCurrentLogin = false;
                displaytext.text = "UserNotFound!!"
                Toast.makeText(this, "Wrong Username or Password, Please Try Again!", Toast.LENGTH_SHORT).show();
            }
        }


        val backtomain: FrameLayout = findViewById(R.id.loginbacktomain)
        backtomain.setOnClickListener{main_home()}
        val registerBtn: FrameLayout = findViewById(R.id.registerBtn)
        registerBtn.setOnClickListener{register_layout()}
    }
    private fun register_layout(){
        setContentView(R.layout.register_layout)

        val backtohome: FrameLayout = findViewById(R.id.singuphomebtn)
        backtohome.setOnClickListener{login_layout()}

        val fullnameReg: EditText = findViewById(R.id.fullnameReg)
        val userNameReg: EditText = findViewById(R.id.userNameReg)
        val userpassreg: EditText = findViewById(R.id.userpassReg)
        val idReg: EditText = findViewById(R.id.idReg)
        val ageReg: EditText = findViewById(R.id.ageReg)

        //Display Output to TextView
        val RegOutput: TextView = findViewById(R.id.userRegOutput)




        val getUserInfo: FrameLayout = findViewById(R.id.userRegNewInfo)
        getUserInfo.setOnClickListener{

            // get User Input from the xml editText Layout

            val userFullName = fullnameReg.text
            val userName = userNameReg.text
            val userPassword = userpassreg.text
            val userId = idReg.text
            val userAge = ageReg.text
            addUserInfo(userFullName.toString(), userName.toString(), userPassword.toString(), userId.toString(), userAge.toString(), RegOutput);
            auth.addUser(userName.toString(), userPassword.toString(), userId.toString(), userAge.toString(), userFullName.toString())
            val (isExists, FullName) = auth.authenticationUser(userName.toString(), userPassword.toString())
            if(isExists){
                Toast.makeText(this@MainActivity, "Success Full Created Account!! User: "+FullName, Toast.LENGTH_SHORT).show()
                login_layout()
            }else{
                Toast.makeText(this@MainActivity, "Failed to Create Account!!", Toast.LENGTH_SHORT).show()
            }

        }


    }

    private fun addUserInfo(userFullName: String, userName: String, userPassword: String, userId: String, userAge: String, display: TextView){
//        var userRegInfoObject: MutableMap<String, String> = mutableMapOf();
        val addUserInfo: (String, String) -> Unit = { key, data ->
            userRegInfoObject.getOrPut(key){ mutableListOf() }.add(data)
        }
        addUserInfo(userFullName, userName)
        addUserInfo(userFullName, userPassword)
        addUserInfo(userFullName, userId)
        addUserInfo(userFullName, userAge)
        display.text = ""+userRegInfoObject
    }

    private fun see_data(){
        setContentView(R.layout.see_data)
        val backtohome: FrameLayout = findViewById(R.id.seeDataBackToHome)
        backtohome.setOnClickListener{main_home()}
    }


    private fun testLayout(){
        setContentView(R.layout.test)
        val backtomain: ImageButton = findViewById(R.id.backtomainBtn);
        backtomain.setOnClickListener{
            Toast.makeText(this@MainActivity, "Back", Toast.LENGTH_SHORT).show()
            setContentView(R.layout.activity_main);
        }
    }
}