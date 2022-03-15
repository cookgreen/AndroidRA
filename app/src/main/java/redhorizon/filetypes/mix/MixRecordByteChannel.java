/*
 * Copyright 2012, Emanuel Rabina (http://www.ultraq.net.nz/)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package redhorizon.filetypes.mix;

import com.android.redalert.BytesAccessReader;
import com.android.redalert.Utils;

import redhorizon.utilities.channels.AbstractDuplicateReadOnlyByteChannel;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

/**
 * Byte channel over a record in the mix file.
 * 
 * @author Emanuel Rabina
 */
public class MixRecordByteChannel extends AbstractDuplicateReadOnlyByteChannel {

	private final FileChannel filechannel;
	private InputStream inputStream;
	private final long lowerbound;
	private final long upperbound;
	private int size;

	private  byte[] bytes;

	/**
	 * Constructor, creates a byte channel backed by the mix file's file
	 * channel.
	 * 
	 * @param inputStream
	 * @param lowerbound
	 * @param size
	 */
	MixRecordByteChannel(InputStream inputStream, int lowerbound, int size) {
		this.filechannel = null;
		this.inputStream = inputStream;

		this.lowerbound  = lowerbound;
		this.upperbound  = lowerbound + size;

		this.size = size;

		position = lowerbound;
	}

	MixRecordByteChannel(ByteBuffer originalBuffer, int lowerbound, int size){
		this.filechannel = null;
		this.lowerbound = lowerbound;
		this.size = size;
		this.upperbound = lowerbound + size;

		bytes = originalBuffer.array();
	}

	MixRecordByteChannel(byte[] recordBytes, int lowerbound, int size)
	{
		this.filechannel = null;
		this.lowerbound = lowerbound;
		this.upperbound = lowerbound+size;
		this.size = size;

		bytes = recordBytes; //Android doesn't support Random File access, so we only use byte array
		position = lowerbound; //Because we already byte array to access the data rather than MixFile's InputStream or FileChannel
	}

	/**
	 * Constructor, creates a byte channel backed by the mix file's file
	 * channel.
	 *
	 * @param filechannel
	 * @param lowerbound
	 * @param size
	 */
	MixRecordByteChannel(FileChannel filechannel, int lowerbound, int size) {

		this.filechannel = filechannel;
		this.lowerbound  = lowerbound;
		this.upperbound  = lowerbound + size;

		position = lowerbound;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isOpenImpl() {

		return filechannel.isOpen();
	}

	public ByteBuffer readBytes(ByteBuffer dst) throws ArrayIndexOutOfBoundsException
	{
		dst.order(ByteOrder.LITTLE_ENDIAN);

		int remaining = (int)(upperbound - position);

		int oldlimit = dst.limit();

		int read = 0;
		byte[] bytes = dst.array();
		bytes = Utils.grabBytes(this.bytes, (int)position, bytes.length);
		position += read; //cursor move forward
		dst = ByteBuffer.wrap(bytes);

		dst.limit(oldlimit);
		return dst;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read(ByteBuffer dst) {
		return -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long size() {

		return upperbound - lowerbound;
	}

	public ByteBuffer getRemainingBytes() {
		return ByteBuffer.wrap(Utils.grabBytes(this.bytes, (int)position, (int)(this.bytes.length - position)));
	}
}
