package com.example.interviewtask.util.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.example.interviewtask.R;


public class BaseCustomDialog<V extends ViewDataBinding> extends Dialog {
    private Context context;
    private V binding;
    private @LayoutRes
    final int layoutId;
    private  Listener listener;

    public BaseCustomDialog(@NonNull Context context, @LayoutRes int layoutId, Listener listener) {
        super(context, R.style.DialogN);
        this.context = context;
        this.layoutId = layoutId;
        this.listener=listener;
    }


    public V getBinding() {
        init();
        return binding;
    }

    private void init() {
        if (binding == null)
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, null, false);
      //  if (listener != null) binding.setVariable(BR.callback, listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(binding.getRoot());
    }

    public interface Listener {
        void onViewClick(View view);
    }
}