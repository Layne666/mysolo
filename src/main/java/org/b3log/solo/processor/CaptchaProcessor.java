/*
 * Solo - A small and beautiful blogging system written in Java.
 * Copyright (c) 2010-2019, b3log.org & hacpai.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.b3log.solo.processor;

import org.apache.commons.lang.StringUtils;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.servlet.HttpMethod;
import org.b3log.latke.servlet.RequestContext;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.servlet.renderer.PngRenderer;
import org.b3log.latke.util.Strings;
import org.patchca.color.GradientColorFactory;
import org.patchca.color.RandomColorFactory;
import org.patchca.filter.predefined.CurvesRippleFilterFactory;
import org.patchca.font.RandomFontFactory;
import org.patchca.service.Captcha;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.word.RandomWordFactory;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Captcha processor.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 2.0.0.5, Sep 21, 2018
 * @since 0.3.1
 */
@RequestProcessor
public class CaptchaProcessor {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(CaptchaProcessor.class);

    /**
     * Key of captcha.
     */
    public static final String CAPTCHA = "captcha";

    /**
     * Captchas.
     */
    private static final Set<String> CAPTCHAS = new HashSet<>();

    /**
     * Captcha length.
     */
    private static final int CAPTCHA_LENGTH = 4;

    /**
     * Flag of captcha is enabled.
     */
    public static boolean CAPTCHA_ON = true;

    /**
     * Captcha chars.
     */
    private static final String CHARS = "acdefhijklmnprstuvwxy234578";

    /**
     * Gets captcha.
     *
     * @param context the specified context
     */
    @RequestProcessing(value = "/captcha", method = HttpMethod.GET)
    public void get(final RequestContext context) {
        final PngRenderer renderer = new PngRenderer();
        context.setRenderer(renderer);

        try {
            final ConfigurableCaptchaService cs = new ConfigurableCaptchaService();
            if (0.5 < Math.random()) {
                cs.setColorFactory(new GradientColorFactory());
            } else {
                cs.setColorFactory(new RandomColorFactory());
            }
            cs.setFilterFactory(new CurvesRippleFilterFactory(cs.getColorFactory()));
            final RandomWordFactory randomWordFactory = new RandomWordFactory();
            randomWordFactory.setCharacters(CHARS);
            randomWordFactory.setMinLength(CAPTCHA_LENGTH);
            randomWordFactory.setMaxLength(CAPTCHA_LENGTH);
            cs.setWordFactory(randomWordFactory);
            cs.setFontFactory(new RandomFontFactory(getAvaialbeFonts()));
            final Captcha captcha = cs.getCaptcha();
            final String challenge = captcha.getChallenge();
            final BufferedImage bufferedImage = captcha.getImage();

            if (CAPTCHAS.size() > 64) {
                CAPTCHAS.clear();
            }

            CAPTCHAS.add(challenge);

            final HttpServletResponse response = context.getResponse();
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);

            try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ImageIO.write(bufferedImage, "png", baos);
                renderer.setImage(baos.toByteArray());
            }
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, e.getMessage(), e);
        }
    }

    /**
     * Checks whether the specified captcha is invalid.
     *
     * @param captcha the specified captcha
     * @return {@code true} if it is invalid, returns {@code false} otherwise
     */
    public static boolean invalidCaptcha(final String captcha) {
        if (!CAPTCHA_ON) {
            return false;
        }

        if (StringUtils.isBlank(captcha) || captcha.length() != CAPTCHA_LENGTH) {
            return true;
        }

        boolean ret = !CaptchaProcessor.CAPTCHAS.contains(captcha);
        if (!ret) {
            CaptchaProcessor.CAPTCHAS.remove(captcha);
        }

        return ret;
    }

    private static List<String> getAvaialbeFonts() {
        final List<String> ret = new ArrayList<>();

        final GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final Font[] fonts = e.getAllFonts();
        for (final Font f : fonts) {
            if (Strings.contains(f.getFontName(), new String[]{"Verdana", "DejaVu Sans Mono", "Tahoma"})) {
                ret.add(f.getFontName());
            }
        }

        final String defaultFontName = new JLabel().getFont().getFontName();
        ret.add(defaultFontName);

        return ret;
    }
}
