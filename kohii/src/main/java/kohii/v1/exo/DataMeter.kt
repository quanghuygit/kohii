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

package kohii.v1.exo

import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.TransferListener

/**
 * Because DefaultBandwidthMeter is final ...
 *
 * @author eneim (2018/06/25).
 */
open class DataMeter<T : BandwidthMeter, S : TransferListener<Any>>(
    private val bandwidthMeter: T,
    private val transferListener: S
) : BandwidthMeter, TransferListener<Any> {

  override fun getBitrateEstimate(): Long {
    return bandwidthMeter.bitrateEstimate
  }

  override fun onTransferStart(source: Any, dataSpec: DataSpec) {
    transferListener.onTransferStart(source, dataSpec)
  }

  override fun onBytesTransferred(source: Any, bytesTransferred: Int) {
    transferListener.onBytesTransferred(source, bytesTransferred)
  }

  override fun onTransferEnd(source: Any) {
    transferListener.onTransferEnd(source)
  }
}
