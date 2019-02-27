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
/**
 * @fileoverview util and every page should be used.
 *
 * @author <a href="http://vanessa.b3log.org">Liyuan Li</a>
 * @version 0.2.0.0, Jan 30, 2019
 */

/**
 * @description 皮肤脚本
 * @static
 */
var Skin = {
  initToc: function () {
    if ($('.b3-solo-list li').length > 0 && $(window).width() > 768) {
      $('.b3-solo-list').css({
        right: '50px',
        'border-right': '1px solid #fff',
        opacity: 1,
      })
      $('#pjax.wrapper').css({
        'max-width': '968px',
        'padding-right': '270px',
      })
    } else {
      $('#pjax.wrapper').removeAttr('style')
    }
  },
  init: function () {
    Skin.initToc()
    Util.initPjax(function () {
      $('.header a').each(function () {
        if (this.href === location.href) {
          this.className = 'current tooltipped tooltipped__w'
        } else {
          this.className = 'tooltipped tooltipped__w'
        }
      })

      Skin.initToc()
    })

    $('.header a').each(function () {
      if (this.href === location.href) {
        this.className = 'current tooltipped tooltipped__w'
      }
    }).click(function () {
      $('.header a').removeClass('current')
      this.className = 'current tooltipped tooltipped__w'
    })

    $('body').on('click', '.content-reset img', function () {
      window.open(this.src)
    })
  },
}
Skin.init()