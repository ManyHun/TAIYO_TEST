package com.erp.taiyo.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;

import com.erp.taiyo.R;


public class IssueQtyDialog {
    private Context context;
    private Dialog dialog;
    private String tvCnt;
   // ListView lvIssueListView;
    int Number;

    EditText etQty;
    Button btnQtyChange, btnClose;


    public IssueQtyDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void call_Level_Dialog(final TextView tvCnt , final String tvFlag , final TextView tvMatTemQty ,final TextView tvTong , final TextView tvIssueQty , final TextView tvRealIssueQty
            ,final TextView tvTemQty, final TextView tvConsiderQty , final TextView  tvTemLosQty) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        dialog = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dialog.setContentView(R.layout.dialog_issue_qty_edit);

        etQty = (EditText) dialog.findViewById(R.id.et_qty);
        btnQtyChange = (Button) dialog.findViewById(R.id.btn_qty_change);
        btnClose = (Button) dialog.findViewById(R.id.btn_close);

       /* DisplayMetrics dm = dialog.getContext().getApplicationContext().getResources().getDisplayMetrics();
        WindowManager.LayoutParams wm = new WindowManager.LayoutParams();
        wm.copyFrom(dialog.getWindow().getAttributes());
        wm.width = dm.widthPixels - (dm.widthPixels / 10);
        wm.height = dm.heightPixels - (dm.widthPixels / 3);
        dialog.getWindow().setAttributes(wm);*/

      //  lvIssueListView = lvListView;
      //  Number = position;
        // 커스텀 다이얼로그를 노출한다.
        dialog.show();

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnQtyChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCnt.setText(etQty.getText().toString());

                try{
                    if(tvFlag.equals("Y")){
                        int matTemQty = Integer.parseInt(tvMatTemQty.getText().toString());
                        int matLosQty = Integer.parseInt(etQty.getText().toString());
                        tvTong.setText(String.valueOf(matTemQty + matLosQty));// 실적 + 로스 = 통
                        // 소수점 2번째 자리까지 출력
                        double total =  (double)(matTemQty + matLosQty) / 500;
                        //tvIssueQty.setText(String.valueOf((matTemQty + matLosQty)/500));// 통/500 해준다
                        tvIssueQty.setText(String.valueOf(total));
                    }else{
                        int realIssueQty = Integer.parseInt(tvRealIssueQty.getText().toString()); //실제 실적 수량
                        int matTemQty = Integer.parseInt(tvMatTemQty.getText().toString());
                        int Qty =Integer.parseInt(tvTemLosQty.getText().toString());

                        tvTong.setText(String.valueOf(matTemQty + Qty));// 실적 + 로스 = 통
                        double total =  (double)(matTemQty + Qty) / 500;
                        tvIssueQty.setText(String.valueOf(total));

                        tvTemQty.setText(String.valueOf(Qty + realIssueQty));
                        tvConsiderQty.setText(String.valueOf(realIssueQty + Qty));// 비교수량 ... 이렇게 짜고싶지 않지만 dialog에서 toast message를 못 보내여,,

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

                dialog.dismiss();
            }
        });

    }


    /*public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
*/

}
