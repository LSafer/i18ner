package net.lsafer.i18ner.internal

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.LongArraySerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.lsafer.i18ner.I18nerInternalApi

@I18nerInternalApi
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

@I18nerInternalApi
class LongRangeStringSerializer : KSerializer<LongRange> {
    private val stringSerializer = String.serializer()

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LongRange", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: LongRange) {
        val string = value.formatLongRangeString()
        encoder.encodeSerializableValue(stringSerializer, string)
    }

    override fun deserialize(decoder: Decoder): LongRange {
        val string = decoder.decodeSerializableValue(stringSerializer)
        val value = string.parseLongRangeString()
        require(value != null) { "Invalid LongRange representation" }
        return value
    }
}
