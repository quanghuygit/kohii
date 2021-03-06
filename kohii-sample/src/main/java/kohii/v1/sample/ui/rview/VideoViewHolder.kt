/*
 * Copyright (c) 2018 Nam Nguyen, nam@ene.im
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kohii.v1.sample.ui.rview

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import kohii.v1.Kohii
import kohii.v1.Playback
import kohii.v1.PlaybackEventListener
import kohii.v1.sample.DemoApp
import kohii.v1.sample.R

/**
 * @author eneim (2018/07/06).
 */
@Suppress("MemberVisibilityCanBePrivate")
class VideoViewHolder(
    inflater: LayoutInflater,
    parent: ViewGroup,
    val listener: OnClickListener
) : BaseViewHolder(inflater, R.layout.holder_player_view, parent),
    View.OnClickListener, PlaybackEventListener, Playback.Callback {

  override fun onTargetAvailable(playback: Playback<*>) {
    listener.onItemLoaded(itemView, adapterPosition)
    ViewCompat.setTransitionName(transView, itemTag)
  }

  override fun onTargetUnAvailable(playback: Playback<*>) {
    ViewCompat.setTransitionName(transView, null)
  }

  override fun onBuffering(playWhenReady: Boolean) {
    Log.i("KohiiApp:VH:$adapterPosition", "onBuffering(): $playWhenReady")
  }

  override fun onPlaying() {
    Log.i("KohiiApp:VH:$adapterPosition", "onPlaying()")
  }

  override fun onPaused() {
    Log.i("KohiiApp:VH:$adapterPosition", "onPaused()")
  }

  override fun onCompleted() {
    Log.i("KohiiApp:VH:$adapterPosition", "onCompleted()")
  }

  val playerView = itemView.findViewById(R.id.playerView) as PlayerView
  val playerContainer = itemView.findViewById(R.id.playerContainer) as AspectRatioFrameLayout
  val transView = playerView.findViewById(R.id.exo_content_frame) as View

  var itemTag: String? = null
  var playback: Playback<PlayerView>? = null

  override fun bind(item: Item?) {
    itemView.setOnClickListener(this)
    if (item != null) {
      itemTag = item.content + "@" + adapterPosition

      playerContainer.setAspectRatio(item.width / item.height.toFloat())
      val playable = Kohii[itemView.context]
          .setUp(item.content)
          .copy(tag = itemTag)
          .copy(repeatMode = Player.REPEAT_MODE_ONE)
          .copy(config = DemoApp.app.config)
          .asPlayable()
      playback = playable.bind(playerView).also {
        it.addPlaybackEventListener(this@VideoViewHolder)
        it.addCallback(this@VideoViewHolder)
      }
    }
  }

  override fun onRecycled(success: Boolean) {
    super.onRecycled(success)
    playback?.removePlaybackEventListener(this)
    playback?.removeCallback(this)
    itemView.setOnClickListener(null)
  }

  override fun onClick(v: View?) {
    if (v != null && itemTag != null) {
      listener.onItemClick(v, transView, adapterPosition, itemTag!!)
    }
  }
}