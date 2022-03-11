/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
/**
 * @author Ilya S. Okomin
 */
package net.windward.android.awt.font;

import net.windward.android.awt.Font;

public interface MultipleMaster {

    public Font deriveMMFont(float[] glyphWidths, float avgStemWidth,
            float typicalCapHeight, float typicalXHeight, float italicAngle);

    public Font deriveMMFont(float[] axes);

    public float[] getDesignAxisDefaults();

    public String[] getDesignAxisNames();

    public float[] getDesignAxisRanges();

    public int getNumDesignAxes();

}

