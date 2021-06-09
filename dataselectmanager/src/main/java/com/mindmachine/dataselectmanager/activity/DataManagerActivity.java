package com.mindmachine.dataselectmanager.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.dataselectmanager.layout.activity_data_manager;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.router.ARouter;
import com.jetpackframework.mvvm.BaseMvvmTitleActivity;
import com.jetpackframework.view.titlebar.TitleBarBuilder;
import com.mindmachine.common.contract.RouterContract;
import com.mindmachine.dataselectmanager.R;
import com.mindmachine.dataselectmanager.R2;
import com.mindmachine.dataselectmanager.adapter.IndexTypeAdapter;
import com.mindmachine.dataselectmanager.bean.TypeListBean;
import com.mindmachine.dataselectmanager.viewmodel.DataManagerViewModel;
import com.yhy.gvp.listener.OnItemClickListener;

import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.util.ArrayList;
import java.util.List;

@ARouter(RouterContract.DATAMANAGERACTIVITY)
@Layout(R2.layout.activity_data_manager)
public class DataManagerActivity extends BaseMvvmTitleActivity<Object, DataManagerViewModel, activity_data_manager> implements OnItemClickListener<TypeListBean> {
    private static final int ROSTER_PERSON = 11;
    private static final int ROSTER_GROUP = 12;
    private static final int TOPIC_IMPORT = 13;
    private static final int DATA_BACKUP = 14;
    private static final int DATA_RESTORE = 15;
    private static final int SCORE_EXPORT = 16;
    private static final int TEMPLATE_EXPORT = 17;
    private static final int TEMPERATURE_EXPORT = 18;
    private static final int VIDEO_BACKUP = 19;

    @Override
    protected Class<DataManagerViewModel> getViewModelClass() {
        return DataManagerViewModel.class;
    }

    @Override
    public TitleBarBuilder setTitleBuilder(TitleBarBuilder titleBarBuilder) {
        return titleBarBuilder.setTitle("数据管理");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final List<TypeListBean> typeDatas = new ArrayList<>();
        String[] typeName = getResources().getStringArray(R.array.data_admin);
        int[] typeRes = new int[]{R.mipmap.icon_data_import, R.mipmap.icon_group_import, R.mipmap.icon_data_down, R.mipmap.icon_position_import, R.mipmap.icon_position_down, R.mipmap.icon_delete_position
                , R.mipmap.icon_data_backup, R.mipmap.icon_data_restore, R.mipmap.icon_data_look, R.mipmap.icon_data_clear, R.mipmap.icon_result_upload,
                R.mipmap.icon_result_import, R.mipmap.icon_template_export, R.mipmap.icon_thermometer, R.mipmap.icon_result_import, R.mipmap.icon_position_down, R.mipmap.icon_data_backup, R.mipmap.icon_data_backup};
        for (int i = 0; i < typeName.length; i++) {
            TypeListBean bean = new TypeListBean();
            bean.setName(typeName[i]);
            bean.setImageRes(typeRes[i]);
            typeDatas.add(bean);
        }
        IndexTypeAdapter indexTypeAdapter = new IndexTypeAdapter(this, typeDatas);//页面内容适配器
        mBinding.grid_viewpager.setGVPAdapter(indexTypeAdapter);
        CommonNavigator commonNavigator = new CommonNavigator(this);//指示器
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                int num = typeDatas.size() / 6;
                if (typeDatas.size() % 6 > 0) {
                    num++;
                }
                return typeDatas == null ? 0 : num;
            }

            @Override
            public IPagerTitleView getTitleView(Context mContext, final int i) {
                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(DataManagerActivity.this);
                View view = View.inflate(DataManagerActivity.this, R.layout.single_image_layout, null);
                final ImageView iv_image = view.findViewById(R.id.iv_image);
                commonPagerTitleView.setContentView(view);//指示器引入外部布局，可知指示器内容可根据需求设置，多样化
                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {
                    @Override
                    public void onSelected(int i, int i1) {
                        iv_image.setImageResource(R.mipmap.icon_point_selected);
                    }

                    @Override
                    public void onDeselected(int i, int i1) {
                        iv_image.setImageResource(R.mipmap.icon_point_unselected);
                    }

                    @Override
                    public void onLeave(int i, int i1, float v, boolean b) {
                    }

                    @Override
                    public void onEnter(int i, int i1, float v, boolean b) {
                    }
                });
                return commonPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        mBinding.indicator_container.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mBinding.indicator_container, mBinding.grid_viewpager);//页面内容与指示器关联
        indexTypeAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position, TypeListBean data) {
        switch (position){
            case 0: //个人名单导入
                arouterActivityForResult(RouterContract.ROUTER_FILEEXPORTACTIVITY,ROSTER_PERSON);
                break;
            case 1:  //分组名单导入
                arouterActivityForResult(RouterContract.ROUTER_FILEEXPORTACTIVITY,ROSTER_GROUP);
                break;
            case 2:  //名单下载
                viewModel.rosterDown();
                break;
            case 3:  //头像导入
                arouterActivityForResult(RouterContract.ROUTER_FILEEXPORTACTIVITY,TOPIC_IMPORT);
                break;
            case 4:  //头像下载
                viewModel.topicDown();
                break;
            case 5:  //删除头像
                viewModel.deleteTopic();
                break;
            case 6:   //数据备份
                arouterActivityForResult(RouterContract.ROUTER_FILEEXPORTACTIVITY,DATA_BACKUP);
                break;
            case 7:   //数据还原
                arouterActivityForResult(RouterContract.ROUTER_FILEEXPORTACTIVITY,DATA_RESTORE);
                break;
            case 8:   //数据查看
                arouterActivity(RouterContract.ROUTER_DATASELECTACTIVITY);
                break;
            case 9:   //数据清空
                viewModel.clearData();
                break;
            case 10:   //成绩上传
                viewModel.scoreUpload();
                break;
            case 11:   //成绩导出
                arouterActivityForResult(RouterContract.ROUTER_FILEEXPORTACTIVITY,SCORE_EXPORT);
                break;
            case 12:   //模板导出
                arouterActivityForResult(RouterContract.ROUTER_FILEEXPORTACTIVITY,TEMPLATE_EXPORT);
                break;
            case 13:  //体温查看
                arouterActivity(RouterContract.ROUTER_TEMPERATURESELECTACTIVITY);
                break;
            case 14:  //体温导出
                arouterActivityForResult(RouterContract.ROUTER_FILEEXPORTACTIVITY,TEMPERATURE_EXPORT);
                break;
            case 15:   //人脸特征检入
                viewModel.faceCheck();
                break;
            case 16:   //备份视频
                arouterActivityForResult(RouterContract.ROUTER_FILEEXPORTACTIVITY,VIDEO_BACKUP);
                break;
            case 17:   //软件更新
                viewModel.softUpdate();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ROSTER_PERSON:
                    viewModel.rosterPerson(data);
                    break;
                case ROSTER_GROUP:
                    viewModel.rosterGroup(data);
                    break;
                case TOPIC_IMPORT:
                    viewModel.topicImport(data);
                    break;
                case DATA_BACKUP:
                    viewModel.dataBackup(data);
                    break;
                case DATA_RESTORE:
                    viewModel.dataRestore(data);
                    break;
                case SCORE_EXPORT:
                    viewModel.scoreExport(data);
                    break;
                case TEMPLATE_EXPORT:
                    viewModel.templateExport(data);
                    break;
                case TEMPERATURE_EXPORT:
                    viewModel.temperatureExport(data);
                    break;
                case VIDEO_BACKUP:
                    viewModel.videoBackup(data);
                    break;
            }
        }
    }
}
