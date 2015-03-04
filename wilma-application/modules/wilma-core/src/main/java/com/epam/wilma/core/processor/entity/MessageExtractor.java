package com.epam.wilma.core.processor.entity;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

This file is part of Wilma.

Wilma is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Wilma is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Wilma.  If not, see <http://www.gnu.org/licenses/>.
===========================================================================*/

import java.io.IOException;

import com.epam.wilma.core.processor.WilmaEntityProcessorInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpEntity;

/**
 * Extract the compressed Wilma messages.
 *
 * @author Balazs_Berkes
 */
@Component
public class MessageExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageExtractor.class);

    @Autowired
    @Qualifier("fastInfosetDecompressorProcessor")
    private WilmaEntityProcessorInterface fastInfosetDecompressorProcessor;
    @Autowired
    @Qualifier("base64DecoderProcessor")
    private WilmaEntityProcessorInterface base64DecoderProcessor;

    /**
     * Extract the given message.
     * @param message message to extract
     */
    public void extract(WilmaHttpEntity message) {
        String body = message.getBody();
        try {
            base64DecoderProcessor.process(message);
            fastInfosetDecompressorProcessor.process(message);
        } catch (ApplicationException e) {
            LOGGER.warn("Failed to extract message content! Restoring original content...", e);
            message.setBody(body);
        } finally {
            closeStream(message);
        }
    }

    private void closeStream(WilmaHttpEntity message) {
        if (message.getInputStream() != null) {
            try {
                message.getInputStream().close();
            } catch (IOException e) {
                LOGGER.error("Failed to close stream!", e);
            }
            message.setInputStream(null);
        }
    }
}