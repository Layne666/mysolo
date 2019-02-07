package org.b3log.solo.processor.player;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.servlet.RequestContext;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RequestProcessor
public class AudioProcessor {

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(AudioProcessor.class);

	/**
	 * @param context the specified http request context
	 */
	@RequestProcessing(value = "/getAudio")
	public void getAudio(final RequestContext context) {
		InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("/playlist.json");
		String json = null;
		try {
			List<Audio> list = new ArrayList<>();
			String content = IOUtils.toString(resourceAsStream, "UTF-8");
			JSONObject obj = JSONObject.fromObject(content);
			JSONArray array = obj.getJSONObject("result").getJSONArray("tracks");
			if (array.size() > 0) {
				for (int i = 0; i < array.size(); i++) {
					Audio audio = new Audio();
					JSONObject job = array.getJSONObject(i); // 遍历 jsonarray数组，把每一个对象转成json 对象
					audio.setArtist(job.getJSONArray("artists").getJSONObject(0).getString("name"));
					audio.setCover(job.getJSONObject("album").getString("picUrl") + "?param=130y130");
					audio.setName(job.getString("name"));
					audio.setUrl("http://music.163.com/song/media/outer/url?id=" + job.getString("id") + ".mp3");
					audio.setLrc("/getLrc?id=" + job.getString("id"));
					// System.out.println(audio);
					list.add(audio);
				}
			}
			json = JSONArray.fromObject(list).toString();
			context.getResponse().getWriter().println(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
