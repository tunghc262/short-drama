//package com.shortdrama.movie.views.bases.ext
//
//import androidx.paging.PagingData
//import androidx.paging.insertSeparators
//import androidx.paging.map
//import com.example.core_api.model.ui.TVSeriesUiModel
//import com.shortdrama.movie.data.models.ForYouModel
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.map
//
//fun Flow<PagingData<TVSeriesUiModel>>.insertNormal():
//        Flow<PagingData<ForYouModel>> {
//
//    return this.map { pagingData ->
//        var index = 0
//
//        pagingData.map { item ->
//            index++
//            ForYouModel.Movie(
//                data = item,
//                displayIndex = index
//            )
//        }
//    }
//}
//
//fun Flow<PagingData<TVSeriesUiModel>>.insertAdsEvery4Items():
//        Flow<PagingData<ForYouModel>> {
//
//    return this.map { pagingData ->
//        var index = 0
//
//        pagingData
//            .map { item ->
//                index++
//                ForYouModel.Movie(
//                    data = item,
//                    displayIndex = index
//                )
//            }
//            .insertSeparators { before, after ->
//                val beforeIndex =
//                    before?.displayIndex
//
//                if (beforeIndex != null && beforeIndex % 4 == 0) {
//                    ForYouModel.Ads
//                } else {
//                    null
//                }
//            }
//    }
//}