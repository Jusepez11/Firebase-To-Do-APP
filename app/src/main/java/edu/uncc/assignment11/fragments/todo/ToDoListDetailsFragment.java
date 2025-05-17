package edu.uncc.assignment11.fragments.todo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.uncc.assignment11.R;
import edu.uncc.assignment11.databinding.FragmentListDetailsBinding;
import edu.uncc.assignment11.databinding.ListItemListItemBinding;
import edu.uncc.assignment11.models.ToDoList;
import edu.uncc.assignment11.models.ToDoListItem;

public class ToDoListDetailsFragment extends Fragment {
    private static final String ARG_PARAM_TODO_LIST= "ARG_PARAM_TODO_LIST";
    FragmentListDetailsBinding binding;
    private ToDoList mToDoList;

    public static ToDoListDetailsFragment newInstance(ToDoList toDoList) {
        ToDoListDetailsFragment fragment = new ToDoListDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_TODO_LIST, toDoList);
        fragment.setArguments(args);
        return fragment;
    }

    public ToDoListDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mToDoList = (ToDoList) getArguments().getSerializable(ARG_PARAM_TODO_LIST);
        }
    }

    ArrayList<ToDoListItem> mToDoListItems = new ArrayList<>();
    ToDoListItemAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("ToDo Lists");

        binding.textViewListName.setText(mToDoList.getName());


        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.todo_list_details_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.add_new_list_item_action){
                    mListener.gotoAddListItem(mToDoList);
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goBackToToDoLists();
            }
        });

        adapter = new ToDoListItemAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        loadToDoListItems();
    }

    void loadToDoListItems(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("todo-list-items").whereEqualTo("list_id", mToDoList.getDoc_id()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    mToDoListItems.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ToDoListItem toDoListItem = document.toObject(ToDoListItem.class);
                        toDoListItem.setDoc_id(document.getId());
                        mToDoListItems.add(toDoListItem);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("demo", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    void deleteToDoListItem(ToDoListItem toDoListItem){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("todo-list-items").document(toDoListItem.getDoc_id())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadToDoListItems();

                    }
                });
    }

    class ToDoListItemAdapter extends RecyclerView.Adapter<ToDoListItemAdapter.ToDoListItemViewHolder>{

        @NonNull
        @Override
        public ToDoListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ListItemListItemBinding itemBinding = ListItemListItemBinding.inflate(getLayoutInflater(), parent, false);
            return new ToDoListItemViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull ToDoListItemViewHolder holder, int position) {
            ToDoListItem toDoListItem = mToDoListItems.get(position);
            holder.bind(toDoListItem);
        }

        @Override
        public int getItemCount() {
            return mToDoListItems.size();
        }

        class ToDoListItemViewHolder extends RecyclerView.ViewHolder{
            ListItemListItemBinding itemBinding;
            ToDoListItem mToDoListItem;
            public ToDoListItemViewHolder(ListItemListItemBinding itemBinding) {
                super(itemBinding.getRoot());
                this.itemBinding = itemBinding;
            }

            public void bind(ToDoListItem toDoListItem) {
                this.mToDoListItem = toDoListItem;

                itemBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteToDoListItem(mToDoListItem);
                    }
                });

                itemBinding.textViewName.setText(toDoListItem.getName());
                itemBinding.textViewPriority.setText(toDoListItem.getPriority());
            }
        }
    }

    ToDoListDetailsListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ToDoListDetailsListener) {
            mListener = (ToDoListDetailsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ToDoListDetailsListener");
        }
    }

    public interface ToDoListDetailsListener {
        void gotoAddListItem(ToDoList toDoList);
        void goBackToToDoLists();
    }
}