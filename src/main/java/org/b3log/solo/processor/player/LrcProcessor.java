package org.b3log.solo.processor.player;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.servlet.RequestContext;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.servlet.renderer.JsonRenderer;
import org.json.JSONObject;


@RequestProcessor
public class LrcProcessor {

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(LrcProcessor.class);

	/**
	 * @param context the specified http request context
	 */
	@RequestProcessing("/getLrc")
	public void sendPost(final RequestContext context) {
		final HttpServletRequest request = context.getRequest();
		String id = request.getParameter("id");
		String result = SendGetRequestUtils.sendGET("http://music.163.com/api/song/lyric", "id="+id+"&lv=1&kv=1");
		final JsonRenderer renderer = new JsonRenderer();
		context.setRenderer(renderer);
		final JSONObject jsonObject = new JSONObject();
		renderer.setJSONObject(jsonObject);
		// 把歌词中的/n去掉
		jsonObject.put("lrc", StringUtils.replace(result, "\\n", ""));
	}

}
