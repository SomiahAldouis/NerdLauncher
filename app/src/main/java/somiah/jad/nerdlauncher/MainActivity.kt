package somiah.jad.nerdlauncher
import android.content.Intent
import android.content.pm.ResolveInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "NerdLauncherActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.app_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        setUpAdapter()
    }

    private fun setUpAdapter(){
        val startUpAdapter = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val activities = packageManager.queryIntentActivities(startUpAdapter,0)
        Log.i(TAG,"Found ${activities.size} activities")

        activities.sortWith(Comparator { a, b ->
            String.CASE_INSENSITIVE_ORDER.compare(
                a.loadLabel(packageManager).toString(),
                b.loadLabel(packageManager).toString()
            )
        })

        recyclerView.adapter = LauncherAdapter(activities)
    }

    private class LauncherHolder(itemView: View):
            RecyclerView.ViewHolder(itemView){

        private val nameTextView = itemView as TextView
       // private val iconApp = itemView as ImageView
        private lateinit var resolveInfo: ResolveInfo

        fun bindLauncher(resolveInfo: ResolveInfo){
            this.resolveInfo = resolveInfo
            val packageManager = itemView.context.packageManager
            val appName = resolveInfo.loadLabel(packageManager).toString()
            //val appIcon = resolveInfo.loadIcon(packageManager)
            nameTextView.text = appName
            //iconApp = appIcon
        }
    }

    private class LauncherAdapter(val activities: List<ResolveInfo>):
            RecyclerView.Adapter<LauncherHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LauncherHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater
                .inflate(android.R.layout.simple_list_item_1, parent, false)
            return LauncherHolder(view)
        }

        override fun getItemCount(): Int {
            return activities.size
        }

        override fun onBindViewHolder(holder: LauncherHolder, position: Int) {
            val resolveInfo = activities[position]
            holder.bindLauncher(resolveInfo)
        }


    }
}