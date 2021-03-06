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

package kohii.v1.sample

import android.app.Application
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.squareup.leakcanary.LeakCanary
import kohii.v1.exo.Config

/**
 * @author eneim (2018/06/26).
 */
class DemoApp : Application() {

  companion object {
    lateinit var app: DemoApp
  }

  override fun onCreate() {
    super.onCreate()
    app = this
    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return
    }
    LeakCanary.install(this)
  }

  val config: Config by lazy {
    Config.DEFAULT_CONFIG.copy(
        cache = SimpleCache(cacheDir, LeastRecentlyUsedCacheEvictor(32 * 1024 * 1024)))
  }
}