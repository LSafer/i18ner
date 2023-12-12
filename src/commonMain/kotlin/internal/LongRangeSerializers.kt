package net.lsafer.i18ner.internal

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.LongArraySerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.lsafer.i18ner.I18nerInternalApi

@I18nerInternalApi
object LongRangeArraySerializer : KSerializer<LongRange> {
    private val longArraySerializer = LongArraySerializer()

    @OptIn(ExperimentalSerializationApi::class)
    override val descriptor = SerialDescriptor(
        serialName = "kotlin.ranges.LongRange",
        original = longArraySerializer.descriptor
    )

    override fun serialize(encoder: Encoder, value: LongRange) {
        val array = longArrayOf(value.first, value.last)
        encoder.encodeSerializableValue(longArraySerializer, array)
    }

    override fun deserialize(decoder: Decoder): LongRange {
        val array = decoder.decodeSerializableValue(longArraySerializer)
        require(array.size == 2) { "Too many elements for a LongRange" }
        return LongRange(array[0], array[1])
    }
}

@I18nerInternalApi
object LongRangeStringSerializer : KSerializer<LongRange> {
    override val descriptor = PrimitiveSerialDescriptor(
        serialName = "kotlin.ranges.LongRange",
        kind = PrimitiveKind.STRING
    )

    override fun serialize(encoder: Encoder, value: LongRange) {
        val string = value.formatLongRangeString()
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): LongRange {
        val string = decoder.decodeString()
        val value = string.parseLongRangeString()
        require(value != null) { "Invalid LongRange representation" }
        return value
    }
}
