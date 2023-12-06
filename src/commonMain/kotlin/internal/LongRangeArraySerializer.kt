package net.lsafer.i18ner.internal

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.LongArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class LongRangeArraySerializer : KSerializer<LongRange> {
    private val longArraySerializer = LongArraySerializer()

    @OptIn(ExperimentalSerializationApi::class)
    override val descriptor: SerialDescriptor = SerialDescriptor("LongRange", longArraySerializer.descriptor)

    override fun serialize(encoder: Encoder, value: LongRange) {
        encoder.encodeSerializableValue(longArraySerializer, longArrayOf(value.first, value.last))
    }

    override fun deserialize(decoder: Decoder): LongRange {
        val array = decoder.decodeSerializableValue(longArraySerializer)
        require(array.size == 2) { "Too many elements for a LongRange" }
        return LongRange(array[0], array[1])
    }
}
