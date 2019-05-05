package com.mirstone.selectfilelib.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.leon.lfilepickerlibrary.utils.FileUtils
import com.mirstone.baselib.base.BaseSwipeBackActivity
import com.mirstone.selectfilelib.R
import com.mirstone.selectfilelib.adapter.PathAdapter
import com.mirstone.selectfilelib.bean.Config
import com.mirstone.selectfilelib.filter.SLFileFilter
import kotlinx.android.synthetic.main.activity_sl_select_file.*
import java.io.File


/**
 * @package: com.mirstone.selectfilelib.ui
 * @fileName: SLSelectFileActivity
 * @data: 2018/7/27 8:32
 * @author: ShiLiang
 * @describe:选择文件
 */
class SLSelectFileActivity : BaseSwipeBackActivity() {
    val ROOT_PATH = Environment.getExternalStorageDirectory().absolutePath
    private lateinit var config: Config
    private lateinit var finishMenu: MenuItem
    private var fileList: List<File> = ArrayList()
    private lateinit var adapter: PathAdapter
    private lateinit var path: String
    private lateinit var filter: SLFileFilter
    private val files = ArrayList<String>()

    private val listNumbers = ArrayList<String>()//存放选中条目的数据地址

    override fun onCreate(savedInstanceState: Bundle?) {
        config = intent.getParcelableExtra<Config>("config")
        path = config.startPath
        setTheme(config.themeId)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sl_select_file)
        initToolbar()
        initRecycler()
        initListener();
    }

    private fun initToolbar() {
        toolbar.title = getString(config.titleId)
        if (config.navigationIcon != -1) {
            toolbar.setNavigationIcon(config.navigationIcon)
        }
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun getStateBarColor(): Int {
        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.toolbar_bg_color, typedValue, true)
        return typedValue.data
    }

    private fun initListener() {
        adapter.setOnItemClickListener({
            if (config.mutiMode) run {
                if (fileList[it].isDirectory) {
                    //如果当前是目录，则进入继续查看目录
                    path = fileList[it].absolutePath
                    files.add(File(path).name)
                    setTextPath(files);
                    checkInDirectory(path)
                } else {
                    //如果已经选择则取消，否则添加进来
                    if (listNumbers.contains(fileList[it].absolutePath)) {
                        listNumbers.remove(fileList[it].absolutePath)
                    } else {
                        //先判断是否达到最大数量，如果数量达到上限提示，否则继续添加
                        if (config.maxNum > 0 && listNumbers.size >= config.maxNum) {
                            adapter.cancelSelect(it)
                            Toast.makeText(this@SLSelectFileActivity, String.format("最多选择%d个文件", config.maxNum), Toast.LENGTH_SHORT).show()
                        } else {
                            listNumbers.add(fileList[it].absolutePath)
                        }
                    }
                    updateMenuTitle()
                }
            } else {
                //单选模式直接返回
                if (fileList[it].isDirectory) {
                    path = fileList[it].absolutePath
                    files.add(File(path).name)
                    setTextPath(files);
                    checkInDirectory(path)
                } else {
                    listNumbers.add(fileList[it].absolutePath)
                    chooseDone()
                }
            }
        })
    }

    private fun setTextPath(files: ArrayList<String>) {
        var path = ""
        for (i in files.indices) {
            val s = files[i]
            if (i == 0) {
                path = s
            } else {
                path += "/$s"
            }
        }
        supportActionBar?.subtitle = path
    }

    private fun updateMenuTitle() {
        if (listNumbers.size == 0) {
            finishMenu.title = getString(config.buttonTextId)
            finishMenu.isEnabled = false
        } else {
            finishMenu.isEnabled = true
            finishMenu.title = String.format("%s(%d/%d)", getString(config.buttonTextId), listNumbers.size, config.maxNum)
        }
    }

    override fun onBackPressed() {
        if (TextUtils.equals(path, ROOT_PATH)) {
            super.onBackPressed()
        } else {
            if (TextUtils.equals(path, "/")) {
                super.onBackPressed()
            } else {
                files.removeAt(files.size - 1)
                setTextPath(files)
                path = File(path).parentFile.absolutePath
                checkInDirectory(path)
            }
        }
    }

    /**
     * 点击进入目录
     *
     * @param position
     */
    private fun checkInDirectory(path: String) {
        //更新数据源
        fileList = FileUtils.getFileList(path, filter, true, 0)
        adapter.setmListData(fileList)
        adapter.notifyDataSetChanged()
        recyclerView.scrollToPosition(0)
    }

    private fun initRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        filter = SLFileFilter(config.types, false)
        adapter = PathAdapter(fileList, this, filter, config.mutiMode, true, 0)
        recyclerView.adapter = adapter
        recyclerView.setmEmptyView(tvEmpty)
        checkInDirectory(path)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_text, menu)
        finishMenu = menu!!.findItem(R.id.menu_text)
        updateMenuTitle()
        finishMenu.isVisible = config.mutiMode
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
            return true
        } else if (item.itemId == R.id.menu_text) {
            chooseDone();
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun chooseDone() {
        val intent = Intent()
        intent.putStringArrayListExtra("paths", listNumbers)
        setResult(Activity.RESULT_OK, intent)
        this.finish()
    }

}
