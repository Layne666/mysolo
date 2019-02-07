<div audioplayer>
	<div id="player8" class="aplayer"></div>
	<script src="https://cdn.jsdelivr.net/npm/jquery"></script>
	<script type="text/javascript">
		if (typeof (ap8) == "undefined") {
			const ap8 = new APlayer({
				element : document.getElementById('player8'),
				mutex : true,
				theme : '#ad7a86',
				order : 'random',
				volume: 0.5,
				lrcType : 3,
				fixed : true,
			});
			$.ajax({
				//url: 'https://api.i-meto.com/meting/api?server=netease&type=playlist&id=35798529',
				url : '/getAudio',
				success : function(list) {
					ap8.list.add(JSON.parse(list));
				}
			});
			//ap8.lrc.hide();
		  } 
	</script>
</div audioplayer>
