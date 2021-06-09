package com.frameworkx.fileexpoter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fileexpoter.layout.fragment_file_export;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnClick;
import com.gwm.annotation.router.ARouter;
import com.gwm.annotation.router.RouterField;
import com.jetpackframework.ContextUtil;
import com.jetpackframework.arouter.Router;
import com.jetpackframework.base.BaseRecyclerAdapter;
import com.jetpackframework.mvvm.BaseMvvmFragment;
import com.mindmachine.common.contract.RouterContract;

import java.util.ArrayList;
import java.util.List;

@ARouter(RouterContract.FILEEXPORTFRAGMENT)
@Layout(R2.layout.fragment_file_export)
public class FileExportFragment extends BaseMvvmFragment<Object,FileViewModel, fragment_file_export> implements BaseRecyclerAdapter.OnItemClickListener {
    @RouterField(value = "path",fieldClass = String.class)
    String path;

    private FileAdapter adapter;
    @Override
    protected Class<FileViewModel> getViewModelClass() {
        return FileViewModel.class;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.loadFile(path);
        adapter = new FileAdapter(new ArrayList<FileBean>());
        mBinding.rv_list.setLayoutManager(new LinearLayoutManager(ContextUtil.get(),LinearLayoutManager.VERTICAL,false));
        mBinding.rv_list.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onChanged(Object o) {
        super.onChanged(o);
        if (o instanceof List){
            adapter.addAll((List<FileBean>) o);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FileBean item = adapter.getItem(position);
        Router.getInstance(getActivity()).from("path",item.absolutPath).to(RouterContract.ROUTER_FILEEXPORTACTIVITY).router();
    }
    private FileBean item;
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        item = adapter.getItem(position);

        return false;
    }

    @OnClick({R2.id.btn_canal,R2.id.btn_commit})
    public void onClick(View view){
        if (view.getId() == R.id.btn_canal){
            mBinding.btn_canal.setVisibility(View.GONE);
            mBinding.btn_commit.setVisibility(View.GONE);
        }else if (view.getId() == R.id.btn_commit){
            Intent intent = new Intent();
            intent.putExtra("filepath",item.absolutPath);
            getActivity().setResult(Activity.RESULT_OK,intent);
            getActivity().finish();
        }
    }
}
