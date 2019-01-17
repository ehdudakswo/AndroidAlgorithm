package dyj.androidalgorithm.adapter

import android.app.ProgressDialog
import android.content.Context
import android.os.Environment
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dyj.androidalgorithm.R
import java.io.File

class MainRecyclerViewAdapter(private val context: Context)
    : RecyclerView.Adapter<MainRecyclerViewHolder>() {

    //RecyclerView
    private val itemList = ArrayList<Item>()
    private lateinit var progressDialog: ProgressDialog

    //Firebase Firestore Info
    private val user = "dyj"
    private val document = "main_recycler_view"
    private val type = "item"

    //Firebase Firestore Item Field
    private val date = "date"
    private val path = "path"
    private val size = "size"

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_main_recycler_view, parent, false)
        view.setOnClickListener {
            showProgressDialog(itemList[viewType])
            downloadFile(itemList[viewType])
        }

        return MainRecyclerViewHolder(view)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
        val item = itemList[position]
        holder.title?.text = filePathSuffix(item)
        holder.content?.text = item.size
    }

    private fun showProgressDialog(item: Item) {
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle(filePathSuffix(item))
        progressDialog.show()
    }

    private fun downloadFile(item: Item) {
        val storageRef = FirebaseStorage.getInstance().reference
        val islandRef = storageRef.child(item.path)
        val dirPath = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).absolutePath
        val localFile = File.createTempFile(
                filePathPrefix(item),
                filePathSuffix(item),
                File(dirPath)
        )

        islandRef.getFile(localFile).addOnSuccessListener {
            val renameFile = File(dirPath, filePathSuffix(item))
            localFile.renameTo(renameFile)
            progressDialog.dismiss()
            Toast.makeText(context, "${renameFile.name} - Download Complete", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            progressDialog.dismiss()
            Toast.makeText(context, "$e - Download Failed", Toast.LENGTH_SHORT).show()
        }.addOnProgressListener {
            val percent = ((it.bytesTransferred.toDouble() / it.totalByteCount) * 100).toInt()
            progressDialog.setMessage("$percent %")
        }
    }

    private fun filePathPrefix(item: Item): String {
        return item.path.split("/").first()
    }

    private fun filePathSuffix(item: Item): String {
        return item.path.split("/").last()
    }

    fun refresh() {
        FirebaseFirestore.getInstance().collection(user).document(document).collection(type)
                .get()
                .addOnCompleteListener { task ->
                    when (task.isSuccessful) {
                        true -> {
                            refresh(task.result!!.documents)
                        }
                        false -> {
                            Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
    }

    private fun refresh(documents: List<DocumentSnapshot>) {
        for (document in documents) {
            itemList.add(Item(
                    document[date].toString(),
                    document[path].toString(),
                    document[size].toString()
            ))
        }

        notifyDataSetChanged()
    }

}

private class Item(val date: String, val path: String, val size: String)

class MainRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val title = itemView.findViewById<TextView?>(R.id.textView_title)
    val content = itemView.findViewById<TextView?>(R.id.textView_content)

}