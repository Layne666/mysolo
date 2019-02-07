package org.b3log.solo.processor.player;

public class Audio {
	/*audio: [{
        name: '光るなら',
        artist: 'Goose house',
        url: 'http://music.163.com/song/media/outer/url?id=547971231.mp3',
        cover: 'http://p1.music.126.net/gWAve6Vnbv0vd6WKa3tGSA==/109951163173817656.jpg?param=130y130',
        theme: '#ebd0c2'
    }]*/
	private String name;
	private String artist;
	private String url;
	private String cover;
	private String lrc;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	
	public void setLrc(String lrc) {
		this.lrc = lrc;
	}
	public String getLrc() {
		return lrc;
	}
	@Override
	public String toString() {
		return "Audio [name=" + name + ", artist=" + artist + ", url=" + url + ", cover=" + cover + ", lrc=" + lrc
				+ "]";
	}
	
}
