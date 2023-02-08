package com.example.virtualtourism

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.example.virtualtourism.R
import com.example.virtualtourism.userdatabase.UserDataBaseHelper

class SignInActivity : AppCompatActivity() {

    private lateinit var mUserDataBase: SQLiteDatabase

    private lateinit var mUserNameEdit: EditText

    private lateinit var mUserAccountEdit: EditText

    private lateinit var mUserAgeEdit: EditText

    private lateinit var mUserPwdEdit: EditText

    private lateinit var mUserSexMale: CheckBox

    private lateinit var mUserSexFemale: CheckBox

    private lateinit var mButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUserDataBase = UserDataBaseHelper(this, "UserInfoDataBase", null, 1).writableDatabase

        setContentView(R.layout.activity_sign_in)

        mUserNameEdit = findViewById(R.id.user_name_edit)

        mUserAccountEdit = findViewById(R.id.user_account_edit)

        mUserPwdEdit = findViewById(R.id.user_pwd_edit)

        mUserAgeEdit = findViewById(R.id.user_age_edit)

        mUserSexMale = findViewById(R.id.user_sex_male)

        mUserSexMale.setOnClickListener {
            mUserSexMale.isChecked = true
            mUserSexFemale.isChecked = false
        }

        mUserSexFemale = findViewById(R.id.user_sex_female)
        mUserSexFemale.setOnClickListener {
            mUserSexMale.isChecked = false
            mUserSexFemale.isChecked = true
        }

        mButton = findViewById(R.id.save_btn)

        mButton.setOnClickListener(onButtonClick)
    }

    private val onButtonClick = View.OnClickListener {
        val account = mUserAccountEdit.text.toString()
        val name = mUserNameEdit.text.toString()
        val pwd = mUserPwdEdit.text.toString()
        val age = mUserAgeEdit.text.toString()
        val sex = if (mUserSexFemale.isChecked) "female"
        else "male"
        saveUserInfo(account, name, age, sex, pwd)
    }


    /**
     * 保存用户信息
     */
    private fun saveUserInfo(
        student_num: String,
        student_name: String,
        student_age: String,
        student_sex: String,
        pwd: String
    ) {
        if (checkUser(student_num)) {
            Toast.makeText(this, "已存在$student_num 用户", Toast.LENGTH_LONG).show()
            return
        }

        val values = ContentValues()
        values.put("student_num", student_num)
        values.put("student_name", student_name)
        values.put("student_age", student_age)
        values.put("student_sex", student_sex)
        values.put("pwd", pwd)
        mUserDataBase.insert(UserDataBaseHelper.TABLE_NAME, null, values)
        Toast.makeText(this, "$student_num 用户注册成功", Toast.LENGTH_LONG).show()
        finish()

    }


    /**
     *
     * @param student_num
     * @return
     */
    private fun checkUser(student_num: String): Boolean {
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
                @SuppressLint("Range") val checkStudentNum =
                    cursor.getString(cursor.getColumnIndex("student_num"))
                Log.d("UserDataBaseHelper", "checkAccount: $checkStudentNum")
                if (checkStudentNum == student_num) {
                    Log.d("UserDataBaseHelper", "checkStudentNum: $checkStudentNum")
                    return true
                }
            } while (cursor.moveToNext())
            cursor.close()
            false
        } catch (e: Exception) {
            Log.d("UserDataBaseHelper", "checkUser error:$e")
            cursor.close()
            false
        }
    }


}