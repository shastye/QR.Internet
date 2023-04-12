package com.example.qrinternet.Activities.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.qrinternet.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // ADDITIONS ADDED BETWEEN COMMENTS

        //TODO: create adapter for clicking qr code to view
        //      Code from C.Gen to modify and work with (this is in kotlin)
        //      Need to make this into a new subclass
        /*
        class PlayerAdapter : BaseAdapter {
            var playerList = ArrayList<Player>()
            var context: Context? = null

            constructor(_context: Context, _playerList: ArrayList<Player>) : super() {
                this.context = _context
                this.playerList = _playerList
            }

            override fun getCount(): Int {
                return playerList.size
            }

            override fun getItem(position: Int): Any {
                return playerList[position]
            }

            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val player = this.playerList[position]

                val inflater =
                    context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val playerView = inflater.inflate(R.layout.image_character_entry, null)
                playerView.findViewById<ImageView>(R.id.gridChild_frame_imageView)
                    .setImageResource(R.drawable.temp_gridview_item)
                playerView.findViewById<TextView>(R.id.gridChild_characterName_textView).text =
                        player._name

                return playerView
            }
        }
        */



        //TODO: get saved qr codes from API



        //TODO: load saved qr codes from API into grid view
        //      Code from C.Gen to modify and work with (this is in kotlin)
        /*
            tempDNDplayers.get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val tempHash: HashMap<String, String> = convertToHashMapStringString(document.data as java.util.HashMap<String, com.google.protobuf.Any>)

                            val tempPlayer = Player(tempHash)
                            playerList.add(tempPlayer)
                        }

                        playerAdapter = PlayerAdapter(this, playerList)
                        findViewById<GridView>(R.id.character_gridView).adapter = playerAdapter
                    }
        */



        // TODO: create grid on click listener
        //      Code from C.Gen to modify and work with (this is in kotlin)
        /*
        val grid = findViewById<GridView>(R.id.character_gridView)
        grid.onItemClickListener = AdapterView.OnItemClickListener { parent, v, position, id ->
            val characterName : String
            val selectedPlayer: Player = playerList[position]

            Toast.makeText(this, " Opening Player: " + selectedPlayer._name,
                Toast.LENGTH_SHORT).show()

            val myIntent = Intent(this, ViewCharacterActivity::class.java)
            myIntent.putExtra("CHARACTER NAME", selectedPlayer._name)
            myIntent.putExtra("CHARACTER TYPE", selectedPlayer._char_type.toString())
            myIntent.putExtra("CHARACTER GAME", selectedPlayer._game_mode.toString())

            startActivity(myIntent)
            finish()
        }
        */



        //

        // END ADDITIONS

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}