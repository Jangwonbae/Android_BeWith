package com.example.bewith.view.search_map.data;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;

public class OfficeArrayListClass {
    public static ArrayList<OfficeData> officeDataArrayList = new ArrayList<>(
            Arrays.asList(
                    new OfficeData("학생회관", new LatLng(37.21224090090654,126.95174643583726)),
                    new OfficeData("교양관", new LatLng(37.2123494466835,126.95235696655)),
                    new OfficeData("도서관", new LatLng(37.21299552053209,126.95243606412339)),
                    new OfficeData("웨슬리관", new LatLng(37.21246154822673,126.95073282276422)),
                    new OfficeData("이공관", new LatLng(37.211905, 126.953162)),
                    new OfficeData("예술관", new LatLng(37.211520707321014,126.95255126155581)),
                    new OfficeData("경영관", new LatLng(37.21307062987013, 126.95343912201486)),
                    new OfficeData("인문사회관", new LatLng(37.21392024503899, 126.95375294803421)),
                    new OfficeData("예술대실습관", new LatLng(37.212571, 126.953686))
                    )
    );
}
