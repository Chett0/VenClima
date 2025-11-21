// package com.example.venclima;

// import android.os.Bundle;
// import android.view.LayoutInflater;
// import android.view.View;
// import android.view.ViewGroup;

// import androidx.annotation.NonNull;
// import androidx.fragment.app.Fragment;
// import androidx.navigation.fragment.NavHostFragment;

// import com.example.venclima.databinding.FragmentSecondBinding;

// public class SecondFragment extends Fragment {

//     private FragmentSecondBinding binding;

//     @Override
//     public View onCreateView(
//             @NonNull LayoutInflater inflater, ViewGroup container,
//             Bundle savedInstanceState
//     ) {

//         binding = FragmentSecondBinding.inflate(inflater, container, false);
//         return binding.getRoot();

//     }

//     public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//         super.onViewCreated(view, savedInstanceState);

//         binding.buttonSecond.setOnClickListener(v ->
//             // action_SecondFragment_to_FirstFragment was removed from nav_graph;
//             // use popBackStack() to return to the previous destination instead.
//             NavHostFragment.findNavController(SecondFragment.this).popBackStack()
//         );
//     }

//     @Override
//     public void onDestroyView() {
//         super.onDestroyView();
//         binding = null;
//     }

// }