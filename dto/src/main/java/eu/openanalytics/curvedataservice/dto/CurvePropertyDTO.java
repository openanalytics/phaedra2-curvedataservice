/**
 * Phaedra II
 *
 * Copyright (C) 2016-2023 Open Analytics
 *
 * ===========================================================================
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Apache License as published by
 * The Apache Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Apache License for more details.
 *
 * You should have received a copy of the Apache License
 * along with this program.  If not, see <http://www.apache.org/licenses/>
 */
package eu.openanalytics.curvedataservice.dto;

import lombok.*;
import lombok.experimental.NonFinal;

@Value
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) // Jackson deserialize compatibility
@NonFinal
public class CurvePropertyDTO {
    Long curveId;
    String name;
    String stringValue;
    Float numericValue;
    byte[] binaryValue;

    public boolean isNumeric() {
        return numericValue != null && stringValue == null && binaryValue == null;
    }

    public boolean isString() {
        return stringValue != null && numericValue == null && binaryValue == null;
    }

    public boolean isBinary() {
        return binaryValue != null && numericValue == null && stringValue == null;
    }
}
