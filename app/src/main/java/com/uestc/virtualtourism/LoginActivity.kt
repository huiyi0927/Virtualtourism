package com.uestc.virtualtourism

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uestc.virtualtourism.ui.theme.VirtualTourismTheme
import com.uestc.virtualtourism.userdatabase.UserDataBaseHelper
import androidx.compose.foundation.Image
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.core.view.WindowCompat
import com.example.virtualtourism.R
import com.lhy.campusnavigator.controller.MapActivity
import com.lt.compose_views.text_field.GoodTextField
import com.lt.compose_views.text_field.HintComposeWithTextField
import com.lt.compose_views.text_field.PasswordTextField

class LoginActivity : ComponentActivity() {

    private lateinit var mUserDataBase: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        mUserDataBase = UserDataBaseHelper(this, "UserInfoDataBase", null, 1).writableDatabase
        setContent {
            VirtualTourismTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Column {
                        UserLoginPagePreview()
                    }
                }
            }
        }
    }


    /**
     *
     * @param student_num
     * @param pwd
     * @return
     */
    private fun checkUser(
        student_num: String,
        pwd: String
    ): Boolean {
        val cursor: Cursor =
            mUserDataBase.query(
                UserDataBaseHelper.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
            )
        return try {
            cursor.moveToFirst()
            do {
                @SuppressLint("Range") val checkAccount =
                    cursor.getString(cursor.getColumnIndex("student_num"))
                Log.d("UserDataBaseHelper", "checkAccount: $checkAccount")
                if (student_num.isNotEmpty() && pwd.isNotEmpty() && checkAccount == student_num) {
                    @SuppressLint("Range") val checkPassWord =
                        cursor.getString(cursor.getColumnIndex("pwd"))
                    Log.d("UserDataBaseHelper", "checkPassWord: $checkPassWord")
                    cursor.close()
                    return if (checkPassWord == pwd) {
                        Toast.makeText(this, "登录成功,进入地图页面", Toast.LENGTH_LONG).show()
                        jumpIntoMapActivity()
                        true
                    } else {
                        Toast.makeText(this, "密码错误", Toast.LENGTH_LONG).show()
                        false
                    }
                }
            } while (cursor.moveToNext())
            cursor.close()
            if (student_num.isEmpty() && pwd.isEmpty()) {
                Toast.makeText(this, "请输入帐号密码", Toast.LENGTH_LONG).show()
                false
            } else {
                Toast.makeText(this, "用户不存在", Toast.LENGTH_LONG).show()
                false
            }


        } catch (e: Exception) {
            Log.d("UserDataBaseHelper", "checkUser error:$e")
            cursor.close()
            false
        }
    }

    private fun startToSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }

    @Preview()
    @Composable
    fun UserLoginPagePreview() {
        BackGround()
    }

    @Composable
    fun BackGround() {
        Box {
            Image(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxSize()
                    .fillMaxWidth(),
                painter = painterResource(R.drawable.main_activity_background),
                contentDescription = "",
                alpha = 0.4f,
                contentScale = ContentScale.FillHeight
            )
            UiTest()
        }
    }


    @Composable
    fun UiTest() {
        Column(
            modifier = Modifier
                .padding(40.dp)
                .fillMaxSize()
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(150.dp),
                    painter = painterResource(R.drawable.tour1),
                    contentDescription = "",
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                modifier = Modifier
                    .width(150.dp)
                    .height(70.dp),
                painter = painterResource(R.drawable.come),
                contentDescription = "",
            )

            var account by remember {
                mutableStateOf("")

            }
            GoodTextField(
                value = account,
                onValueChange = { account = it },
                hint = HintComposeWithTextField.createTextHintCompose("请输入帐号"),
                modifier = Modifier
                    .padding(8.dp)
                    .height(60.dp)
                    .fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(20.dp))
            var pwd by remember { mutableStateOf("") }
            var passwordIsShow by remember { mutableStateOf(false) }
            PasswordTextField(
                value = pwd,
                onValueChange = {
                    pwd = it
                },
                hint = HintComposeWithTextField.createTextHintCompose("请输入密码"),
                passwordIsShow = passwordIsShow,
                onPasswordIsShowChange = { passwordIsShow = it },
                modifier = Modifier
                    .padding(8.dp)
                    .height(60.dp)
                    .fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(90.dp))
            Column {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        startToSignIn()
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF03A9F4)),
                    contentPadding = PaddingValues(12.dp, 16.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp),
                        painter = painterResource(R.drawable.img1675699476173),
                        contentDescription = "",
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        checkUser(account, pwd)
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF03A9F4)),
                    contentPadding = PaddingValues(12.dp, 16.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(
                                20
                                    .dp
                            ),
                        painter = painterResource(R.drawable.img1675699483152),
                        contentDescription = "",
                    )
                }
            }

        }

    }

    fun jumpIntoMapActivity() {
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
        finish()
    }

}