package com.woonggon.sqllitecrudtutorial

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Memo(var no: Int, var content: String, var datetime: Long)

class SqliteHelper(context: Context, name: String, version: Int) :
    SQLiteOpenHelper(context, name, null, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        val create = "create table memo (`no` integer primary key, content text, datetime integer)"
        db?.execSQL(create)
    }

    override fun onUpgrade(
        p0: SQLiteDatabase?,
        p1: Int,
        p2: Int
    ) {
    }

    // 데이터 입력 함수
    fun insertMemo(memo:Memo) {
        // db 가져오기
        val wd = writableDatabase
        val values = ContentValues()
        values.put("content", memo.content)
        values.put("datetime", memo.datetime)

        wd.insert("memo", null, values)
        wd.close()
    }

    // 데이터 조회 함수

    fun selectMemo(): MutableList<Memo>
    {
        val list = mutableListOf<Memo>()
        val select = "select * from memo"
        val rd = readableDatabase
        rd.rawQuery(select, null)
        val cursor = rd.rawQuery(select, null)
        while (cursor.moveToNext())
        {
            val no = cursor.getInt(cursor.getColumnIndex("no"))
            val content = cursor.getString(cursor.getColumnIndex("content"))
            val datetime = cursor.getLong(cursor.getColumnIndex("datetime"))

            val memo = Memo(no, content, datetime)
            list.add(memo)
        }
        cursor.close()
        rd.close()

        return list
    }

    fun updateMemo(memo: Memo){
        val wd = writableDatabase
        val values = ContentValues()
        values.put("content", memo.content)
        values.put("datetime", memo.datetime)
        wd.update("memo", values, "no=${memo.no}", null)
        wd.close()

    }

    fun deleteMemo(memo: Memo)
    {
        val delete = "delete from memo where no = ${memo.no}"
        val wd = writableDatabase
        wd.execSQL(delete)
        wd.close()
    }




}
