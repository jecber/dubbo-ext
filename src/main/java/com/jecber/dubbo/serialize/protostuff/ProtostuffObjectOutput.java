/**
 * Copyright 1999-2014 dangdang.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jecber.dubbo.serialize.protostuff;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.serialize.Cleanable;
import com.alibaba.dubbo.common.serialize.ObjectOutput;
import io.protostuff.LinkedBuffer;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ProtostuffObjectOutput implements ObjectOutput, Cleanable {

	private final OutputStream stream;

	private static final Logger logger = LoggerFactory.getLogger(ProtostuffObjectOutput.class);

	private static ThreadLocal<LinkedBuffer> linkedBuffer = new ThreadLocal<LinkedBuffer>() {
		@Override
		protected LinkedBuffer initialValue() {
			return LinkedBuffer.allocate(500);
		}
	};

	public ProtostuffObjectOutput(OutputStream stream) {
		this.stream = stream;
	}

	// 默认不超过三位即可
	private byte[] int2Byte(int data) {
		byte[] result = new byte[3];
		result[0] = (byte) (data & 0xFF);
		result[1] = 0;
		result[2] = 0;
		int higer = data >> 8;
		if (higer > 0) {
			result[1] = (byte) (higer & 0xFF);
			int top = data >> 16;
			if (top > 0) {
				result[2] = (byte) (top & 0xFF);
			}
		}
		return result;
	}

	public void writeObject(Object obj) throws IOException {
		try {

			Schema schema = null;
			byte[] bodyBytes = null;

			if (obj instanceof Map) {
				MapEntity entity = new MapEntity((Map) obj);
				schema = RuntimeSchema.getSchema(MapEntity.class);
				bodyBytes = ProtostuffUtils.toByteArray(entity, schema,
						linkedBuffer.get());
			} else if (obj instanceof Collection) {
				if (obj instanceof List) {// 可以少一次判断
					ListEntity entity = new ListEntity((List) obj);
					schema = RuntimeSchema.getSchema(ListEntity.class);
					bodyBytes = ProtostuffUtils.toByteArray(entity, schema,
							linkedBuffer.get());
				} else if (obj instanceof Set) {
					SetEntity entity = new SetEntity((Set) obj);
					schema = RuntimeSchema.getSchema(SetEntity.class);
					bodyBytes = ProtostuffUtils.toByteArray(entity, schema,
							linkedBuffer.get());
				}
			} else {
				schema = RuntimeSchema.getSchema(obj.getClass());
				bodyBytes = ProtostuffUtils.toByteArray(obj, schema,
						linkedBuffer.get());
			}

			// 记录长度。
			byte[] lengthBytes = int2Byte(bodyBytes.length);
			stream.write(lengthBytes);
			stream.write(bodyBytes);

		} finally {
			linkedBuffer.get().clear();
		}
	}

	public void writeByte(byte v) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("now in writeBytes method... ");
		}
		stream.write(v);
	}

	public void writeUTF(String v) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("now in writeUTF method... ");
		}
		// 按照先存大小，后存内容的方式存储。
		stream.write(v.length());
		stream.write(v.getBytes());
	}

	public void flushBuffer() throws IOException {
		stream.flush();
	}

	@Deprecated
	public void writeBool(boolean v) throws IOException {
		writeObject(v);

	}

	@Deprecated
	public void writeShort(short v) throws IOException {
		writeObject(v);
	}

	@Deprecated
	public void writeInt(int v) throws IOException {
		writeObject(v);
	}

	@Deprecated
	public void writeLong(long v) throws IOException {
		writeObject(v);
	}

	@Deprecated
	public void writeFloat(float v) throws IOException {
		writeObject(v);
	}

	@Deprecated
	public void writeDouble(double v) throws IOException {
		writeObject(v);

	}

	@Deprecated
	public void writeBytes(byte[] v) throws IOException {

	}

	@Deprecated
	public void writeBytes(byte[] v, int off, int len) throws IOException {

	}

	@Deprecated
	public void cleanup() {

	}

}