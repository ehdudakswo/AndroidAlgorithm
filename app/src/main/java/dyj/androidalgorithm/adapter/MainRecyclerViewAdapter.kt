package dyj.androidalgorithm.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.firestore.DocumentSnapshot
import dyj.androidalgorithm.R

class MainRecyclerViewAdapter(private val context: Context)
    : RecyclerView.Adapter<MainRecyclerViewHolder>() {

    private val itemList = ArrayList<Item>()
    private val date = "date"
    private val title = "title"
    private val content = "content"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
        return MainRecyclerViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_main_recycler_view, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
        val item = itemList[position]
        holder.date?.text = item.date
        holder.title?.text = item.title
        holder.content?.text = item.content
    }

    fun refresh(documents: List<DocumentSnapshot>) {
        for (document in documents) {
            itemList.add(Item(
                    document[date].toString(),
                    document[title].toString(),
                    document[content].toString()
            ))
        }

        notifyDataSetChanged()
    }

}

private class Item(val date: String, val title: String, val content: String)

class MainRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val date = itemView.findViewById<TextView?>(R.id.textView_date)
    val title = itemView.findViewById<TextView?>(R.id.textView_title)
    val content = itemView.findViewById<TextView?>(R.id.textView_content)

}