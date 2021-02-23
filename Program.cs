using System;
using System.IO;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;

namespace Spring_Multipart_Related
{
    class Program
    {
        static int port = 8080;
        static String host = "localhost";
        static String endpoint = "multiRelated";

        static void Main(string[] args)
        {
            Console.WriteLine("Begin Request");

            string pathXml = @"./src/main/resources/Metadata.xml";
            string strXml = File.ReadAllText(pathXml,Encoding.UTF8);

            string pathPdfA = @"./src/main/resources/IntegratingOutboundFaxServices.pdf";
            byte[] bPdfA = File.ReadAllBytes(pathPdfA);

            string pathPdfB = @"./src/main/resources/ProposedArchitecture.pdf";
            byte[] bPdfB = File.ReadAllBytes(pathPdfB);

            MultipartContent multipart = new MultipartContent("related");
            // multipart.Headers.ContentType = new MediaTypeHeaderValue("multipart/related");
            HttpContent contentXml = new StringContent(strXml,Encoding.UTF8,"text/xml");
            StreamContent contentPdfA = new StreamContent(new MemoryStream(bPdfA));
            contentPdfA.Headers.ContentType = new MediaTypeHeaderValue("application/pdf");
            StreamContent contentPdfB = new StreamContent(new MemoryStream(bPdfB));
            contentPdfB.Headers.ContentType = new MediaTypeHeaderValue("application/pdf");
            multipart.Add(contentXml);
            multipart.Add(contentPdfA);
            multipart.Add(contentPdfB);

            HttpClient http = new HttpClient();
            http.DefaultRequestHeaders.TransferEncodingChunked = true;
            HttpRequestMessage request = new HttpRequestMessage(HttpMethod.Post,String.Format("http://{0}:{1}/{2}",host,port,endpoint));
            request.Content = multipart;
            http.Send(request);

            Console.WriteLine("End Request");
        }
    }
}
