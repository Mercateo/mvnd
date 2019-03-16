package com.mercateo.oss.mvnd;

import static org.junit.Assert.fail;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.mockito.Mockito;

import com.mercateo.oss.mvnd.MVNDProto.InvokeResponse.ResponseType;

import io.grpc.stub.StreamObserver;
import junit.framework.Assert;

public class RemoteStreamWriterTest {

	@Test
	public void emptyStreamFlushReturns() throws Exception {

		PipedInputStream i = new PipedInputStream();
		PipedOutputStream o = new PipedOutputStream(i);
		RemoteStreamWriter uut = new RemoteStreamWriter(i, Mockito.mock(StreamObserver.class), ResponseType.OUT);

		uut.flush();

	}

	@Test
	public void flushBlocksUntilISFullyRead() throws Exception {

		PipedInputStream i = new PipedInputStream();
		PipedOutputStream o = new PipedOutputStream(i);
		RemoteStreamWriter uut = new RemoteStreamWriter(i, Mockito.mock(StreamObserver.class), ResponseType.OUT);

		o.write(1);

		CompletableFuture<Boolean> f = CompletableFuture.supplyAsync(() -> {
			uut.flush();
			return true;
		});
		// expected to block
		try {
			f.get(100, TimeUnit.MILLISECONDS);
			Assert.fail("call was expected to block");
		} catch (TimeoutException ok) {
		}

		i.read();
		// now, it should be empty and flush ok
		uut.flush();
	}

	@Test(timeout=1000)
	public void runUntilClosed() throws Exception {
		PipedInputStream i = new PipedInputStream();
		PipedOutputStream o = new PipedOutputStream(i);
		RemoteStreamWriter uut = new RemoteStreamWriter(i, Mockito.mock(StreamObserver.class), ResponseType.OUT);

		PrintWriter pw = new PrintWriter(o);
		pw.println("test234");
		CompletableFuture<Void> copyThread = CompletableFuture.runAsync(() -> {
			uut.run();
		});

		wontComplete(copyThread);
		
		pw.flush();
		wontComplete(copyThread);
		
		pw.close();
		
		// now, it should be completed
		copyThread.get();
	}

	private void wontComplete(CompletableFuture<Void> copyThread) throws InterruptedException, ExecutionException {
		try {
			copyThread.get(50, TimeUnit.MILLISECONDS);
			fail("was not supposed to be completed, just yet");
		} catch (TimeoutException ok) {
		}
	}
}
