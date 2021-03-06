/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.kafka.streams.kstream;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class WindowsTest {

    private class TestWindows extends Windows {

        @Override
        public Map windowsFor(final long timestamp) {
            return null;
        }

        @Override
        public long size() {
            return 0;
        }
    }

    @SuppressWarnings("deprecation") // specifically testing deprecated APIs
    @Test
    public void shouldSetNumberOfSegments() {
        final int anySegmentSizeLargerThanOne = 5;
        final TestWindows testWindow = new TestWindows();
        final long maintainMs = testWindow.maintainMs();

        assertEquals(
            maintainMs / (anySegmentSizeLargerThanOne - 1),
            testWindow.segments(anySegmentSizeLargerThanOne).segmentInterval()
        );
    }

    @SuppressWarnings("deprecation") // specifically testing deprecated APIs
    @Test
    public void shouldSetWindowRetentionTime() {
        final int anyNotNegativeRetentionTime = 42;
        assertEquals(anyNotNegativeRetentionTime, new TestWindows().until(anyNotNegativeRetentionTime).maintainMs());
    }


    @Test
    public void gracePeriodShouldEnforceBoundaries() {
        new TestWindows().grace(0L);

        try {
            new TestWindows().grace(-1L);
            fail("should not accept negatives");
        } catch (final IllegalArgumentException e) {
            //expected
        }
    }

    @SuppressWarnings("deprecation") // specifically testing deprecated APIs
    @Test(expected = IllegalArgumentException.class)
    public void numberOfSegmentsMustBeAtLeastTwo() {
        new TestWindows().segments(1);
    }

    @SuppressWarnings("deprecation") // specifically testing deprecated APIs
    @Test(expected = IllegalArgumentException.class)
    public void retentionTimeMustNotBeNegative() {
        new TestWindows().until(-1);
    }

}
