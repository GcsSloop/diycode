$(function() {
    marked.setOptions({
        langPrefix: ''
    });
    preview = function setMarkdown(md_text){
        if(md_text == "") return false;
        md_text = md_text.replace(/\\n/g, "\n");
        var md_html = marked(md_text);
        $('#preview').html(md_html);
        $('pre code').each(function(i, block) {
            hljs.highlightBlock(block);
        });

        var objs = document.getElementsByTagName("img");
            for(var i=0;i<objs.length;i++) {
            window.listener.collectImage(objs[i].src);
            objs[i].onclick=function() {
               window.listener.onImageClicked(this.src);
            }
        }
    };
});