package dyj.androidalgorithm.activity

import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import dyj.androidalgorithm.R
import dyj.androidalgorithm.adapter.MainRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_content.*
import kotlinx.android.synthetic.main.activity_main_nav_view_test.*
import kotlinx.android.synthetic.main.activity_main_toolbar.*


class MainActivity : AppCompatActivity() {

    private val user = "dyj"
    private val document = "main_recycler_view"
    private val type = "item"
    private val recyclerViewAdapter = MainRecyclerViewAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setNavigationView()
        setOnClickListener()
        setRecyclerView()
        loadFirebaseFirestore()
    }

    private fun setNavigationView() {
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun setOnClickListener() {
        nav_view_test.setOnClickListener {
            Toast.makeText(this, "nav_view_test", Toast.LENGTH_LONG).show()
        }
    }

    private fun setRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerViewAdapter
    }

    private fun loadFirebaseFirestore() {
        FirebaseFirestore.getInstance().collection(user).document(document).collection(type)
                .get()
                .addOnCompleteListener { task ->
                    when (task.isSuccessful) {
                        true -> {
                            recyclerViewAdapter.refresh(task.result!!.documents)
                        }
                        false -> {
                            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                }

    }

}