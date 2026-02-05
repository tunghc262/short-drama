package com.example.core_api.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.core_api.model.ui.TVSeriesUiModel
import com.example.core_api.repository.MoviesRepository
import retrofit2.HttpException
import java.io.IOException

class TVSeriesPagingSource(
    private val repository: MoviesRepository
) : PagingSource<Int, TVSeriesUiModel>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, TVSeriesUiModel> {
        return try {
            val page = params.key ?: 1
            val data = repository.getTVSeries(page)

            LoadResult.Page(
                data = data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1
            )
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: IOException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(
        state: PagingState<Int, TVSeriesUiModel>
    ): Int? {
        return state.anchorPosition?.let { pos ->
            state.closestPageToPosition(pos)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(pos)?.nextKey?.minus(1)
        }
    }
}
